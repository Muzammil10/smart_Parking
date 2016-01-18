package org.smartparking.smartparking;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {

    public Button launchgoogleView;
    public Button saveplaceView;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        launchgoogleView = (Button) findViewById(R.id.btn_launch_map);
        saveplaceView = (Button) findViewById(R.id.btn_saveplace);
        test=(TextView) findViewById(R.id.textView2);

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
            startActivity(new Intent(this, LoginOrSignUp.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////// FONCTIONS AJOUTEES//////////


    // Affiche les places libres
    public void btn_google(View view) {

        Toast.makeText(getApplicationContext(), "Lancement Google Map", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, MapsActivity.class));

    }

    // Sauvegarde une place libre sur la position actuelle
    public void btn_sauvegardeplace(View view) {

        // Fonction qui récupère la position actuelle et la stocke dans la base de données

        // Lance le service de localisation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Récupération de la latitude et la longittude

        // ATTENTION IL FAUT GERER LES PERMISSIONS (regarder favori sur gps)

        latitude = locationManager.getLastKnownLocation("gps").getLatitude();
        longitude = locationManager.getLastKnownLocation("gps").getLongitude();

        //Maintenant il faut stocker la longtitude et la latitude dans la base de données (donc dans Parse)

        //On crée l'objet place libre dans la base de données Parse
        final ParseObject places_libres = new ParseObject("Places_Libre");
        places_libres.put("latitude", latitude);
        places_libres.put("longitude", longitude);
        places_libres.saveInBackground();

      /*  ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        places_libres.put("Location", point);
        places_libres.saveInBackground();*/



        // Récupère les coordonnées (latitude et longitutde) de toutes les places libres.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places_Libre");
        query.getInBackground("ZdB4Ot7wj2", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    // object will be your game score
                    test.append("\nok"+ parseObject.getDouble("latitude")+ " " + parseObject.getDouble("latitude"));
                } else {
                    // something went wrong
                    test.append("\nerrror");
                }
            }
        });


        Toast.makeText(getApplicationContext(), "Place Libre Sauvegardée", Toast.LENGTH_LONG).show();

    }
}
