package org.smartparking.smartparking;

import android.content.Intent;
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

                    // Redirection vers la page d'accueuil de l'application
                    startActivityForResult(new Intent(LogInActivity.this,AccueilActivity.class), 1000);

                }
            }
        });

    }

    ////////////////// RECUPERE LE RESULTAT DE L'ACTIVITE SUIVANTE ET SE FEMRE CI BESOIN"
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // on récupère le statut de retour de l'activité 2 c'est à dire l'activité numéro 1000
        if(requestCode==1000){
            // si le code de retour est égal à 1 on stoppe l'activité 1
            if(resultCode==1){
                // ferme l'actviité
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
