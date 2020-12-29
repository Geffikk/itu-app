package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forumandroid.Adapters.GroupAdapter;
import com.example.forumandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private TextView textView;

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
        textView = findViewById(R.id.toolbar_text);

        textView.setText("Groups");

        updateGroups();
        updateDrawerProfile();
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeDrawer(drawerLayout);
    }

    private void updateGroups() {
        db.collection("groups")
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
        openDrawer(drawerLayout);
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

    public void clickSettings() {
        redirectActivity(this, SettingsActivity.class);
    }

    public void clickLogout() {
        logout(this);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void logout(Activity activity) {
        activity.finish();
        System.exit(0);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }
}