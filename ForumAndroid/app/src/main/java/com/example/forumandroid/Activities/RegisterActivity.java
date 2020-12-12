package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forumandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    // Declare Firebase Auth
    private FirebaseAuth firebaseAuth;

    // Declare widgets
    private EditText registerName, registerEmail, registerPassword, registerPasswordCheck;
    private Button registerButton;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize widgets
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.loginEmail);
        registerPassword = findViewById(R.id.loginPassword);
        registerPasswordCheck = findViewById(R.id.registerPasswordCheck);
        registerButton = findViewById(R.id.loginButton);
        registerProgressBar = findViewById(R.id.loginProgressBar);

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

                // TODO acc failed create information messages
            }
            else {
                CreateAccount(registerEmailText, registerPasswordText, registerNameText);
            }
        });
    }

    private void CreateAccount(String registerEmailText, String registerPasswordText, String registerNameText) {
        firebaseAuth.createUserWithEmailAndPassword(registerEmailText, registerPasswordText).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                /* Registration successful */
                showMessage("Account successfully created.");
                updateUserName(registerNameText, firebaseAuth.getCurrentUser());
            }
            else {
                /* Registration not successful */
                showMessage("Failed to create an account.");
            }
        });
    }

    private void updateUserName(String registerNameText, FirebaseUser currentUser) {

    }

    private void showMessage(String msg) {

        // Set registerButton Visible
        registerButton.setVisibility(View.VISIBLE);
        registerProgressBar.setVisibility(View.INVISIBLE);

        // Throw Toast msg
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}


/*

service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}

*/