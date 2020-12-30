package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forumandroid.Adapters.GroupAdapter;
import com.example.forumandroid.Adapters.ThreadAdapter;
import com.example.forumandroid.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.forumandroid.Activities.HomeActivity.closeRightDrawer;
import static com.example.forumandroid.Activities.HomeActivity.showMessage;

public class GroupActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private ListView listView;
    private DrawerLayout drawerLayout;
    private EditText threadName;
    private EditText threadDescription;

    // for debugging
    private final String TAG = HomeActivity.class.getSimpleName();

    private String groupName;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        listView = findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout);
        threadName = findViewById(R.id.threadName);
        threadDescription = findViewById(R.id.threadDescription);

        // Set toolbar text
        TextView textView = findViewById(R.id.toolbar_text);
        textView.setText("Group");

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        groupName = getIntent().getExtras().getString("groupName");

        // Get group ID from db
        db.collection("groups")
                .whereEqualTo("name", groupName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            groupId = document.getId();
                            updateThreads();
                        }
                    }
                });

        updateDrawerProfile();
    }

    public void clickMenu(View view){
        HomeActivity.openLeftDrawer(drawerLayout);
    }

    public void clickedAddThread(View view) {
        HomeActivity.openRightDrawer(drawerLayout);
    }

    public void clickedCreateThread(View view) {
        threadName = findViewById(R.id.threadName);
        threadDescription = findViewById(R.id.threadDescription);
        String threadNameText = threadName.getText().toString();
        String threadDescriptionText = threadDescription.getText().toString();

        if ( threadNameText.isEmpty() || threadDescriptionText.isEmpty() ) {
            /* Empty input */
            showMessage("All fields are required.", getApplicationContext());
            return;
        }

        CreateThread(firebaseAuth.getCurrentUser().getUid(), groupId, new Timestamp(new Date()), threadNameText, threadDescriptionText);
    }

    private void CreateThread(String author, String group, Timestamp date, String description, String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("author", author);
        data.put("date", date);
        data.put("group", group);
        data.put("description", description);
        data.put("name", name);

        db.collection("threads")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Thread created successfully", getApplicationContext());

                    Intent intent = new Intent(this, ThreadActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("threadName", name);
                    intent.putExtras(bundle);

                    startActivity(intent);

                    threadName.setText("");
                    threadDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Thread no created. Reason: " + e, getApplicationContext());
                });
    }

    public void clickMore(View view) { }

    public void clickHome(View view) {
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void clickProfile(View view) {
        HomeActivity.redirectActivity(this, ProfileActivity.class);
    }

    public void clickSettings() {
        HomeActivity.redirectActivity(this, SettingsActivity.class);
    }

    public void clickLogout() {
        HomeActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        HomeActivity.closeLeftDrawer(drawerLayout);
        closeRightDrawer(drawerLayout);
    }

    private void updateThreads() {
        db.collection("threads")
                .whereEqualTo("group", groupId)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> arrayList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            arrayList.add(document.getString("name"));

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }

                        ThreadAdapter threadAdapter = new ThreadAdapter(GroupActivity.this, arrayList);
                        listView.setAdapter(threadAdapter);
                    } else {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "No threads to display");
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateDrawerProfile() {
        TextView navName = findViewById(R.id.userName);
        TextView navEmail = findViewById(R.id.userEmail);

        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (! task.isSuccessful()) {
                Exception e = task.getException();
                navName.setText(e.getMessage());
            }
            else{
                navName.setText(task.getResult().getString("name"));
            }
        });

        navEmail.setText(firebaseAuth.getCurrentUser().getEmail());
    }
}