package org.smartparking.smartparking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
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
    private Button autosave;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private float vitesse;
    private int flag_manuelle_save=0;
    private int flag_autosave=0;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        launchgoogleView = (Button) findViewById(R.id.btn_launch_map);
        saveplaceView = (Button) findViewById(R.id.btn_saveplace);
        autosave=(Button) findViewById(R.id.btn_autosave);
        test = (TextView) findViewById(R.id.textView2);

        btn_automatique_save();

        // Lance le service de localisation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creation d'un listerner pour update la localisation
        locationListener = new LocationListener() {
            @Override
            //Fonction appelé quand il y a un changement de lattitude ou longitude.
            public void onLocationChanged(Location location) {
                //Stock la latitude et la longtitude
                latitude=location.getLatitude();
                longitude=location.getLongitude();

                // Permet de récupérer la vitesse de l'utilisateur grâce au GPS
                vitesse =location.getSpeed();
                test.append("Vitesse enregistré:"+vitesse+"\n");

                // AJOUTE MANUELLEMENT UNE PLACE
                if (flag_manuelle_save==1) {
                    // Stock la latitude et la longitutde dans la base de données
                    final ParseObject places_libres = new ParseObject("Places_Libres");
                    // On crée un Geopoint pour l'utiliser par la suite si nécessaire

                    ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
                    // On crée ces 2 colonnes pour facilité la récupération de ces 2 attributs

                    places_libres.put("latitude", latitude);
                    places_libres.put("longitude", longitude);
                    places_libres.put("Location", point);
                    places_libres.saveInBackground();


                    Toast.makeText(getApplicationContext(), "Place Libre Sauvegardée", Toast.LENGTH_LONG).show();

                    //Arrete l'écoute si on enregistre manuellement la place
                    // Attention, ne pas oublier de remettre le flag à 0
                    flag_manuelle_save = 0;
                    locationManager.removeUpdates(locationListener);
                }


                // AJOUTE AUTOMATIQUEMENT UNE PLACE
                if (flag_autosave==1) {

                    // Préviens du lancement du service
                    Toast.makeText(getApplicationContext(), "Activation du service d'ajout automatique de places", Toast.LENGTH_LONG).show();
                    // Attention, ne pas oublier de remettre le flag à 0
                    flag_autosave = 0;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
                };

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                        },10);
                    }
                    //return;
                } else {
                    btn_sauvegardeplace();
                }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    btn_sauvegardeplace();
                }
               // return;
        }
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

    // Sauvegarde une place libre grâce à la position actuelle
    public void btn_sauvegardeplace() {

        saveplaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_manuelle_save=1;
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });
    }

    // Sauvegarde automatiquement une place grâce à la vitesse
    public void btn_automatique_save() {

        autosave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_autosave=1;
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });

    }


}
