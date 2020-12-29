package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.forumandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        textView = findViewById(R.id.toolbar_text);

        textView.setText("Profile");

        updateDrawerProfile();
    }

    public void clickMenu(View view) {
        HomeActivity.openDrawer(drawerLayout);
    }

    public void clickMore(View view) { }

    public void clickHome(View view) {
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void clickProfile(View view) {
        recreate();
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

        HomeActivity.closeDrawer(drawerLayout);
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