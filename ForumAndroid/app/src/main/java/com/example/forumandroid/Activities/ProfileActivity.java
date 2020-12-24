package com.example.forumandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import com.example.forumandroid.R;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawerLayout);
    }

    public void clickMenu(View view) {
        HomeActivity.openDrawer(drawerLayout);
    }

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
}