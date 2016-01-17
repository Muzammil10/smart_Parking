package org.smartparking.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    Button signup;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////// Base de donneés Parse /////////
        //Fait le lien entre la bdd et l'application
        Parse.enableLocalDatastore(this);
        //Initialisation de la base de données
        Parse.initialize(this);
        //Création d'un objet test
        ParseObject testObject = new ParseObject("TestObject");
        //Ajout des données
        testObject.put("foo", "prout");
        //Eguivalent d'un update dans la bdd
        testObject.saveInBackground();



        // Affectation des variables : boutton, Textview
        signup=(Button) findViewById(R.id.id_signup);
        login=(Button) findViewById(R.id.id_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////////////// Fonction ajoutées (gestions actions : boutons, itents) ///////////////

    // Fonction boutton log in
    public void Login(View v ){
        // Message envoyé à l'utilisateur
        Toast.makeText(getApplicationContext(), "Connectez vous", Toast.LENGTH_LONG).show();

        //Lancement Intent pour Login
        Intent intend= new Intent(this,LogInActivity.class);
        startActivity(intend);
    }

    // Fonction boutton SignUp
    public void Signup(View v) {
        // Message envoyé à l'utilisateur
        Toast.makeText(getApplicationContext(), "Enregistrez vous", Toast.LENGTH_LONG).show();

        //Lancement Intent pour Sign up
        Intent intend= new Intent(this,SignUpActivity.class);
        startActivity(intend);
    }
}
