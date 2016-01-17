package org.smartparking.smartparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

//Activité permettant de gérer le login des userse dans l'application
public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ParseUser.logInInBackground("my name", "my pass", new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (e != null) {
                    // Hooray! The user is logged in.

                    // message test
                    Toast.makeText(getApplicationContext(), "Mauvais password", Toast.LENGTH_LONG).show();

                } else {
                    // Signup failed. Look at the ParseException to see what happened.

                    // message test
                    Toast.makeText(getApplicationContext(), "Vous êtes log", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
