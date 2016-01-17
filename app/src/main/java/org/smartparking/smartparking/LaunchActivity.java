package org.smartparking.smartparking;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            startActivity(new Intent(this,AccueilActivity.class));
        } else {
            // show the signup or login screen

            startActivity(new Intent(this, LoginOrSignUp.class));
        }

    }
}
