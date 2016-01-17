package org.smartparking.smartparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;


// ACTIVITE POUR SIGN UP
public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");


        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Accès à l'application, donc on revient à la page : Signup or Log in
                } else {
                    // Sign up n'a pas marché, texte vide etc.
                }
            }
        });

    }
}
