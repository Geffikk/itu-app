package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.forumandroid.R;

public class SettingsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        drawerLayout = findViewById(R.id.drawerLayout);

        textView = findViewById(R.id.toolbar_text);

        textView.setText("Settings");
    }

    public void clickMenu(View view) {
        //HomeActivity.openDrawer(drawerLayout);
    }

    public void clickMore(View view) { }

    public void clickHome(View view) {
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void clickProfile(View view) {
        HomeActivity.redirectActivity(this, ProfileActivity.class);
    }

    public void clickSettings() {
        recreate();
    }

    public void clickLogout() {
        HomeActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //HomeActivity.closeDrawer(drawerLayout);
    }
}