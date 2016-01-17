package org.smartparking.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;


// ACTIVITE POUR SIGN UP
public class SignUpActivity extends AppCompatActivity {
    //On les déclare en private pour que ça n'influe pas sur LoginActivity
    private EditText usernameView;
    private EditText passwordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameView= (EditText) findViewById(R.id.username);
        passwordView= (EditText) findViewById(R.id.password);


    }
    /////////////////////// Fonctionss (OnClick, etc) /////////////////////

    // Quand on appuie sur le bouton Sign Up
    public void btn_SignUp(View view) {

        ParseUser user = new ParseUser();
        user.setUsername(usernameView.getText().toString());
        user.setPassword(passwordView.getText().toString());
        //Optionnel, mais on peut rajouter l'email au besoin
        //user.setEmail("email@example.com");

        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // On est enregistré donc on retourne à l'activité "LoginOrSignUp"
                    finish();
                } else {
                    // Sign up n'a pas marché, texte vide etc.
                }
            }
        });

    }
}
