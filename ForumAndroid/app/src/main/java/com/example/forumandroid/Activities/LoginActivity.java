package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forumandroid.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Declare Firebase Auth
    private FirebaseAuth firebaseAuth;

    // Declare widgets
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private TextView loginTextView;

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
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize widgets
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginTextView = findViewById(R.id.loginTextView);

        loginProgressBar.setVisibility(View.INVISIBLE);

        // Set login button click
        loginButton.setOnClickListener(view -> {
            loginButton.setVisibility(View.INVISIBLE);
            loginProgressBar.setVisibility(View.VISIBLE);

            final String loginEmailText = loginEmail.getText().toString();
            final String loginPasswordText = loginPassword.getText().toString();

            if ( loginEmailText.isEmpty() || loginPasswordText.isEmpty() ) {
                /* Empty input */
                showMessage("All fields are required.");
            }
            else {
                LoginAccount(loginEmailText, loginPasswordText);
            }
        });

        // Set text view redirect to register activity
        loginTextView.setOnClickListener(view -> showRegisterActivity());
    }

    private void LoginAccount(String loginEmailText, String loginPasswordText) {
        firebaseAuth.signInWithEmailAndPassword(loginEmailText, loginPasswordText).addOnCompleteListener(task -> {
            if ( task.isSuccessful() ) {
                showHomeActivity();
            }
            else {
                showMessage("Login unsuccessful. Reason: " + task.getException().getMessage());
            }
        });
    }

    private void showHomeActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void showRegisterActivity() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    private void showMessage(String msg) {

        // Set loginButton Visible
        loginButton.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.INVISIBLE);

        // Throw Toast msg
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}