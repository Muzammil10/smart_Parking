package org.smartparking.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

public class AccueilActivity extends AppCompatActivity {

    public Button launchgoogleView;
    public Button saveplaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        launchgoogleView= (Button) findViewById(R.id.btn_launch_map);
        saveplaceView=(Button) findViewById(R.id.btn_saveplace);
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
        if (id == R.id.logout) {

            //////////// PERMET DE SE LOG OUT, A MODIFIER PAR LA SUITE /////////

            // Log out l'utilisateur
            ParseUser.getCurrentUser().logOut();

            // On retourne sur la Page Log in or Signup
            // Set result permet d'envoyer la "sortie" de l'activité à l'activité inférieure.
            setResult(1);
            startActivity (new Intent(this,LoginOrSignUp.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////// FONCTIONS AJOUTEES//////////


    // Affiche les places libres
    public void btn_google(View view) {

        Toast.makeText(getApplicationContext(), "Lancement Google Map", Toast.LENGTH_LONG).show();

        startActivity(new Intent (this, MapsActivity.class));

    }

    // Sauvegarde une place libre sur la position actuelle
    public void btn_sauvegardeplace(View view) {

        Toast.makeText(getApplicationContext(), "Place Libre Sauvegardée", Toast.LENGTH_LONG).show();
    }
}
