package com.confidenceb.spamsmsdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Nav_Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_hamburg);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.Close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.confidenceb.spamsmsdetector.Messages()).commit();
            navigationView.setCheckedItem(R.id.OpenMsgs);
        }

    }

    public void userLogOut(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Nav_Drawer.this);
        builder.setMessage("Do you want to log out?");
        builder.setCancelable(true);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    @Override
    public void onBackPressed () {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Dashboard:
                Log.i("INFO","User dashboard");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.confidenceb.spamsmsdetector.Dashboard()).commit();
                break;

            case R.id.OpenMsgs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.confidenceb.spamsmsdetector.Messages()).commit();
                break;

            case R.id.SpamMsgs:
                Log.i("INFO","Spam");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.confidenceb.spamsmsdetector.ArchiveMessages()).commit();
                break;

            case R.id.Notifications:
                Log.i("INFO","Notification");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new com.confidenceb.spamsmsdetector.Notifications()).commit();
                break;

            case R.id.ShareApp:
                shareApp();
                break;

            case R.id.Logout:
                userLogOut();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}