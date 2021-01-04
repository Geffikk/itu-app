package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.forumandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.forumandroid.Activities.HomeActivity.closeRightDrawer;
import static com.example.forumandroid.Activities.HomeActivity.showMessage;

public class ProfileActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    // Debugging tool
    private final String TAG = ProfileActivity.class.getSimpleName();

    DrawerLayout drawerLayout;
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
        updateUserInfo();
    }

    private void updateUserInfo() {
        EditText firstname = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText mobile = findViewById(R.id.mobile);
        EditText city = findViewById(R.id.city);
        EditText bio = findViewById(R.id.bio);

        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(task -> {
            String firstnameText = task.getString("firstname");
            String surnameText = task.getString("surname");
            String mobileText = task.getString("mobile");
            String cityText = task.getString("city");
            String bioText = task.getString("bio");

            firstname.setText( (firstnameText == null) ? "" : firstnameText);
            surname.setText( (surnameText == null) ? "" : surnameText);
            mobile.setText( (mobileText == null) ? "" : mobileText);
            city.setText( (cityText == null) ? "" : cityText);
            bio.setText( (bioText == null) ? "" : bioText);
        });
    }

    public void clickedUpdateUserInfo(View view) {
        EditText firstname = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText mobile = findViewById(R.id.mobile);
        EditText city = findViewById(R.id.city);
        EditText bio = findViewById(R.id.bio);

        Map<String, Object> data = new HashMap<>();
        data.put("firstname", firstname.getText().toString());
        data.put("surname", surname.getText().toString());
        data.put("mobile", mobile.getText().toString());
        data.put("city", city.getText().toString());
        data.put("bio", bio.getText().toString());

        db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .update(data)
                .addOnSuccessListener(documentReference -> {
                    HomeActivity.showMessage("Profile updated successfully", getApplicationContext());
                    updateUserInfo();
                })
                .addOnFailureListener(e -> {
                    HomeActivity.showMessage("Profile couldn't be updated", getApplicationContext());
                });
    }

    public void clickMenu(View view) {
        HomeActivity.openLeftDrawer(drawerLayout);
    }

    public void clickMore(View view) { }

    public void clickHome(View view) {
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void clickProfile(View view) {
        recreate();
    }

    public void clickSettings(View view) {
        HomeActivity.redirectActivity(this, SettingsActivity.class);
    }

    public void clickLogout(View view) {
        HomeActivity.logout(this, firebaseAuth);
    }

    @Override
    protected void onPause() {
        super.onPause();

        HomeActivity.openLeftDrawer(drawerLayout);
        HomeActivity.openRightDrawer(drawerLayout);
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