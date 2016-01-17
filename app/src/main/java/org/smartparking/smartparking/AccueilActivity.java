package org.smartparking.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
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

            // Permet de log out l'utilisateur
            ParseUser.getCurrentUser().logOut();
            /*
            // Regarde si l'utilisateur est connecté
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // do stuff with the user
                Toast.makeText(getApplicationContext(), "user connected", Toast.LENGTH_LONG).show();
            } else {
                // show the signup or login screen
                Toast.makeText(getApplicationContext(), "No one present", Toast.LENGTH_LONG).show();
            }
            */
            // On retourne sur la Page Log in or Signup
            setResult(1);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
