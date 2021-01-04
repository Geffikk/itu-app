package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forumandroid.Adapters.GroupAdapter;
import com.example.forumandroid.Adapters.ThreadAdapter;
import com.example.forumandroid.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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

    // Debugging tool
    private final String TAG = GroupActivity.class.getSimpleName();

    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        listView = findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout);
        threadName = findViewById(R.id.threadName);
        threadDescription = findViewById(R.id.threadDescription);

        String groupName = getIntent().getExtras().getString("groupName");

        // Set toolbar text
        TextView textView = findViewById(R.id.toolbar_text);
        textView.setText(groupName);

        // set visible MORE on toolbar
        ImageView imageView = findViewById(R.id.toolbarMore);
        imageView.setVisibility(View.VISIBLE);

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get group ID from db
        db.collection("groups")
                .whereEqualTo("name", groupName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            groupId = document.getId();
                            updateThreads();
                            updateDescription();
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

        CreateThread(firebaseAuth.getCurrentUser().getUid(), groupId, new Timestamp(new Date()), threadDescriptionText, threadNameText);
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

                    EditText editTextThreadName = findViewById(R.id.threadName);
                    EditText editTextThreadDescription = findViewById(R.id.threadDescription);
                    editTextThreadName.setText("");
                    editTextThreadDescription.setText("");

                    recreate();
                })
                .addOnFailureListener(e -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Thread no created. Reason: " + e, getApplicationContext());
                });
    }

    public void deleteGroup(View view) {

        db.collection("groups").document(groupId)
                .get()
                .addOnCompleteListener(task -> {

                    if ( task.isSuccessful() ) {

                        DocumentSnapshot document = task.getResult();

                        String authorId = document.getString("author");

                        if ( firebaseAuth.getCurrentUser().getUid().equals(authorId) ) {
                            db.collection("groups").document(groupId).delete();
                            finish();
                        }
                        else {
                            showMessage("You are not owner.", getApplicationContext());
                        }
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void clickMore(View view) {
        LinearLayout linearLayout = findViewById(R.id.deleteGroup);

        if ( linearLayout.getVisibility() == View.VISIBLE ) {
            linearLayout.setVisibility(View.GONE);
        }
        else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void clickHome(View view) {
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void clickProfile(View view) {
        HomeActivity.redirectActivity(this, ProfileActivity.class);
    }

    public void clickSettings(View view) {
        HomeActivity.redirectActivity(this, SettingsActivity.class);
    }

    public void clickLogout(View view) {
        HomeActivity.logout(this, firebaseAuth);
    }

    public void clickThread(View view) {
        Intent intent = new Intent(this, ThreadActivity.class);

        TextView textView = view.findViewById(R.id.textView);
        String textViewText = textView.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("threadName", textViewText);
        intent.putExtras(bundle);

        startActivity(intent);

        threadName.setText("");
        threadDescription.setText("");
    }

    public void clickedShowDescription(View view) {
        LinearLayout linearLayout = findViewById(R.id.descriptionLayoutGroups);
        ImageView imageView = findViewById(R.id.ImageViewShowDescriptionGroup);

        if ( linearLayout.getVisibility() == View.VISIBLE ) {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        HomeActivity.closeLeftDrawer(drawerLayout);
        closeRightDrawer(drawerLayout);
    }

    private void updateDescription() {
        db.collection("groups").document(groupId)
                .get()
                .addOnCompleteListener(task -> {

                    if ( task.isSuccessful() ) {

                        DocumentSnapshot document = task.getResult();

                        TextView textViewDescription = findViewById(R.id.descriptionGroups);
                        TextView textViewNameAndDate = findViewById(R.id.nameAndDateGroups);

                        String authorId = document.getString("author");
                        String descriptionText = document.getString("description");
                        textViewDescription.setText(descriptionText);

                        db.collection("users").document(authorId)
                                .get()
                                .addOnCompleteListener(task2 -> {

                                    if ( task2.isSuccessful() ) {
                                        DocumentSnapshot document2 = task2.getResult();

                                        String authorName = document2.getString("name");

                                        textViewNameAndDate.setText("By " + authorName);
                                    }
                                    else {
                                        Log.d(TAG, "Error getting documents: ", task2.getException());
                                    }
                                });
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
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

                        TextView noThreads = findViewById(R.id.no_threads);

                        if (arrayList.size() < 1) {
                            noThreads.setVisibility(View.VISIBLE);
                        }
                        else {
                            noThreads.setVisibility(View.GONE);
                            ThreadAdapter threadAdapter = new ThreadAdapter(GroupActivity.this, arrayList);
                            listView.setAdapter(threadAdapter);
                        }
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