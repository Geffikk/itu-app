package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forumandroid.Adapters.GroupAdapter;
import com.example.forumandroid.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private EditText groupName;
    private EditText groupDescription;

    // For debugging
    private final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        listView = findViewById(R.id.listView);
        TextView textView = findViewById(R.id.toolbar_text);
        groupName = findViewById(R.id.groupName);
        groupDescription = findViewById(R.id.groupDescription);

        textView.setText("Groups");

        updateGroups();
        updateDrawerProfile();
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeLeftDrawer(drawerLayout);
        closeRightDrawer(drawerLayout);
    }

    private void updateGroups() {
        db.collection("groups")
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> arrayList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            arrayList.add(document.getString("name"));
                        }

                        // Parity guard preventing NullPointerException
                        if ( arrayList.size() % 2 != 0 ) {
                            arrayList.add("");
                        }

                        GroupAdapter groupAdapter = new GroupAdapter(HomeActivity.this, arrayList);
                        listView.setAdapter(groupAdapter);
                    } else {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "No groups to display");
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

    public void clickMenu(View view){
        openLeftDrawer(drawerLayout);
    }

    public void clickedAddGroup(View view) {
        openRightDrawer(drawerLayout);
    }

    public void clickedCreateGroup(View view) {
        groupName = findViewById(R.id.groupName);
        groupDescription = findViewById(R.id.groupDescription);
        String groupNameText = groupName.getText().toString();
        String groupDescriptionText = groupDescription.getText().toString();

        if ( groupNameText.isEmpty() || groupDescriptionText.isEmpty() ) {
            /* Empty input */
            showMessage("All fields are required.", getApplicationContext());
            return;
        }

        CreateGroup(firebaseAuth.getCurrentUser().getUid(), new Timestamp(new Date()), groupDescriptionText, groupNameText);
    }

    private void CreateGroup(String author, Timestamp date, String description, String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("author", author);
        data.put("date", date);
        data.put("description", description);
        data.put("name", name);

        db.collection("groups")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Group created successfully", getApplicationContext());

                    Intent intent = new Intent(this, GroupActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("groupName", name);
                    intent.putExtras(bundle);

                    startActivity(intent);

                    EditText editTextGroupName = findViewById(R.id.groupName);
                    EditText editTextGroupDescription = findViewById(R.id.groupDescription);
                    editTextGroupName.setText("");
                    editTextGroupDescription.setText("");

                    recreate();
                })
                .addOnFailureListener(e -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Group no created. Reason: " + e, getApplicationContext());
                });
    }

    public void clickMore(View view) {}

    public void clickHome(View view) {
        recreate();
    }

    public void clickProfile(View view) {
        redirectActivity(this, ProfileActivity.class);
    }

    public void clickGroupLeft(View view) {
        TextView textViewGroup = view.findViewById(R.id.textViewLeft);

        String groupName = textViewGroup.getText().toString();

        Intent intent = new Intent(this, GroupActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void clickGroupRight(View view) {
        TextView textViewGroup = view.findViewById(R.id.textViewRight);
        String groupName = textViewGroup.getText().toString();

        Intent intent = new Intent(this, GroupActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void clickSettings(View view) {
        redirectActivity(this, SettingsActivity.class);
    }

    public void clickLogout(View view) {
        logout(this, firebaseAuth);
    }

    public static void openLeftDrawer(DrawerLayout drawerLayout) {
        if ( ! drawerLayout.isDrawerOpen(GravityCompat.END) ) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public static void openRightDrawer(DrawerLayout drawerLayout) {
        if ( ! drawerLayout.isDrawerOpen(GravityCompat.START) ) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public static void closeLeftDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void closeRightDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    public static void logout(Activity activity, FirebaseAuth firebaseAuth) {
        firebaseAuth.signOut();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finishAffinity();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void showMessage(String msg, Context context) {
        // Throw Toast msg
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}