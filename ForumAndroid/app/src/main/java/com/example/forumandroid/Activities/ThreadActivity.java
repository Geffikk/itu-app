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

import com.example.forumandroid.Adapters.PostAdapter;
import com.example.forumandroid.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.forumandroid.Activities.HomeActivity.closeRightDrawer;
import static com.example.forumandroid.Activities.HomeActivity.showMessage;

public class ThreadActivity extends AppCompatActivity {

    // Declare Firebase Auth and firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private ListView listView;
    private DrawerLayout drawerLayout;
    private EditText post;
    private TextView description;
    private TextView nameAndDate;

    // for debugging
    private final String TAG = ThreadActivity.class.getSimpleName();

    private String threadName;
    private String threadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        listView = findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout);
        description = findViewById(R.id.description);
        nameAndDate = findViewById(R.id.nameAndDate);
        threadName = getIntent().getExtras().getString("threadName");

        // Set toolbar text
        TextView textView = findViewById(R.id.toolbar_text);
        textView.setText(threadName);

        // Initialize Firebase Auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get thread ID from db
        db.collection("threads")
                .whereEqualTo("name", threadName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            threadId = document.getId();
                            updateDescription();
                            updatePosts();
                        }
                    }
                });

        updateDrawerProfile();
    }

    public void clickMenu(View view){
        HomeActivity.openLeftDrawer(drawerLayout);
    }

    public void clickedAddPost(View view) {
        HomeActivity.openRightDrawer(drawerLayout);
    }

    public void clickedShowDescription(View view) {
        LinearLayout linearLayout = findViewById(R.id.descriptionLayout);
        ImageView imageView = findViewById(R.id.ImageViewShowDescription);

        if ( linearLayout.getVisibility() == View.VISIBLE ) {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void clickedCreatePost(View view) {
        post = findViewById(R.id.post);
        String postText = post.getText().toString();

        if ( postText.isEmpty() ) {
            /* Empty input */
            showMessage("Write a comment first.", getApplicationContext());
            return;
        }

        CreatePost(firebaseAuth.getCurrentUser().getUid(), new Timestamp(new Date()), postText);
    }

    private void CreatePost(String authorId, Timestamp date, String post) {
        Map<String, Object> data = new HashMap<>();
        data.put("author", authorId);
        data.put("date", date);
        data.put("post", post);
        data.put("thread", threadId);

        db.collection("posts")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Post created successfully", getApplicationContext());

                    Intent intent = new Intent(this, ThreadActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("threadName", threadName);
                    intent.putExtras(bundle);

                    startActivity(intent);

                    EditText editText = findViewById(R.id.post);
                    editText.setText("");

                    finish();
                })
                .addOnFailureListener(e -> {
                    closeRightDrawer(drawerLayout);
                    showMessage("Post no created. Reason: " + e, getApplicationContext());
                });
    }

    public void clickMore(View view) {
        LinearLayout linearLayout = findViewById(R.id.deleteThread);

        if ( linearLayout.getVisibility() == View.VISIBLE ) {
            linearLayout.setVisibility(View.GONE);
        }
        else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void deleteThread(View view) {

        db.collection("threads").document(threadId)
                .get()
                .addOnCompleteListener(task -> {

                    if ( task.isSuccessful() ) {

                        DocumentSnapshot document = task.getResult();

                        String authorId = document.getString("author");

                        if ( firebaseAuth.getCurrentUser().getUid().equals(authorId) ) {
                            db.collection("threads").document(threadId).delete();
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

    @Override
    protected void onPause() {
        super.onPause();

        HomeActivity.closeLeftDrawer(drawerLayout);
        closeRightDrawer(drawerLayout);
    }

    private void updateDescription() {
        db.collection("threads").document(threadId)
                .get()
                .addOnCompleteListener(task -> {

                    if ( task.isSuccessful() ) {

                        DocumentSnapshot document = task.getResult();

                        String authorId = document.getString("author");
                        Timestamp date = document.getTimestamp("date");
                        String nameAndDateText = date.toDate().toString();

                        String descriptionText = document.getString("description");
                        description.setText(descriptionText);

                        db.collection("users").document(authorId)
                                .get()
                                .addOnCompleteListener(task2 -> {

                                    if ( task2.isSuccessful() ) {
                                        DocumentSnapshot document2 = task2.getResult();

                                        String authorName = document2.getString("name");

                                        nameAndDate.setText("By " + authorName + " at " + nameAndDateText);
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


    private void updatePosts() {
        db.collection("posts")
                .whereEqualTo("thread", threadId)
                .orderBy("date")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> posts = new ArrayList<>();
                        ArrayList<String> authors = new ArrayList<>();
                        ArrayList<String> dates = new ArrayList<>();
                        ArrayList<String> authorsNames = new ArrayList<>();

                        Log.d(TAG, String.valueOf(task.getResult().size()));

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            posts.add(document.getString("post"));
                            authors.add(document.getString("author"));
                            Timestamp timestamp = document.getTimestamp("date");
                            dates.add(timestamp.toDate().toString());
                        }

                        TextView noPosts = findViewById(R.id.no_posts);

                        if (posts.size() < 1) {
                            noPosts.setVisibility(View.VISIBLE);
                        }
                        else {
                            noPosts.setVisibility(View.GONE);

                            db.collection("users")
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            for (String id : authors) {
                                                for (QueryDocumentSnapshot document : task2.getResult()) {
                                                    if (document.getId().equals(id)) {
                                                        authorsNames.add(document.getString("name"));
                                                        break;
                                                    }
                                                }
                                            }

                                            PostAdapter postAdapter = new PostAdapter(ThreadActivity.this, posts, authorsNames, dates);
                                            listView.setAdapter(postAdapter);
                                        }
                                    });
                        }
                    } else {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "No posts to display");
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