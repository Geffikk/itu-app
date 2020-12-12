package com.example.forumandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forumandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseStore;

    // Declare widgets
    private EditText registerName, registerEmail, registerPassword, registerPasswordCheck;
    private Button registerButton;
    private ProgressBar registerProgressBar;
    private TextView registerTextView;

    @Override
    protected void onStart() {
        super.onStart();

//        if ( firebaseAuth.getCurrentUser() != null ) {
//            /* User already logged in */
//            showHomeActivityAndFinish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();

        // Initialize widgets
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPasswordCheck = findViewById(R.id.registerPasswordCheck);
        registerButton = findViewById(R.id.registerButton);
        registerProgressBar = findViewById(R.id.registerProgressBar);
        registerTextView = findViewById(R.id.registerTextView);

        registerProgressBar.setVisibility(View.INVISIBLE);

        // Implement Button click
        registerButton.setOnClickListener(view -> {
            registerButton.setVisibility(View.INVISIBLE);
            registerProgressBar.setVisibility(View.VISIBLE);

            final String registerNameText = registerName.getText().toString();
            final String registerEmailText = registerEmail.getText().toString();
            final String registerPasswordText = registerPassword.getText().toString();
            final String registerPasswordCheckText = registerPasswordCheck.getText().toString();

            if ( registerNameText.isEmpty() || registerEmailText.isEmpty() || registerPasswordText.isEmpty() || registerPasswordCheckText.isEmpty() ) {
                /* Empty input */
                showMessage("All fields are required.");
            }
            else if( ! registerPasswordText.equals(registerPasswordCheckText)) {
                showMessage("Passwords does not match.");
            }
            else {
                CreateAccount(registerEmailText, registerPasswordText, registerNameText);
            }
        });

        // Set text view redirect to login activity
        registerTextView.setOnClickListener(view -> showLoginActivity());
    }

    private void CreateAccount(String registerEmailText, String registerPasswordText, String registerNameText) {
        firebaseAuth.createUserWithEmailAndPassword(registerEmailText, registerPasswordText).addOnCompleteListener(this, task -> {
            if ( task.isSuccessful() ) {
                /* Registration successful */
                showMessage("Account successfully created.");
                updateUserName(registerNameText, firebaseAuth.getCurrentUser().getUid());
                showHomeActivity();
            }
            else {
                /* Registration not successful */
                showMessage("Failed to create an account. Reason: " + task.getException().getMessage());
            }
        });
    }

    private void showHomeActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void showLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void updateUserName(String registerNameText, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", registerNameText);

        firebaseStore.collection("users").document(userId).set(user);
    }

    private void showMessage(String msg) {

        // Set registerButton Visible
        registerButton.setVisibility(View.VISIBLE);
        registerProgressBar.setVisibility(View.INVISIBLE);

        // Throw Toast msg
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}