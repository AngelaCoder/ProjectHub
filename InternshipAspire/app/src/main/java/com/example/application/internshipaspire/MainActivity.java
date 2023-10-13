package com.example.application.internshipaspire;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the ImageViews (icons)
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView musicIcon = findViewById(R.id.musicIcon);
        ImageView calculatorIcon = findViewById(R.id.calculatorIcon);
        ImageView logoutIcon = findViewById(R.id.logoutIcon);

        // Set click listeners for the icons
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Screen1Activity
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        musicIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Screen2Activity
                Intent intent = new Intent(MainActivity.this, Music.class);
                startActivity(intent);
            }
        });

        calculatorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Screen3Activity
                Intent intent = new Intent(MainActivity.this, NumericCalculator.class);
                startActivity(intent);
            }
        });

        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut(); // Sign the user out

        // Create an intent to navigate to the login screen (replace "Login.class" with your actual login activity class)
        Intent intent = new Intent(getApplicationContext(), Login.class);

        // Start the login activity
        startActivity(intent);

        // Finish the current activity (which should be the bottom navigation activity)
        finish();
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "Yes," perform the logout action
                signOut();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "No," close the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}