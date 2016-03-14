package org.smartparking.smartparking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.WifiConfiguration;
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

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class AccueilActivity extends AppCompatActivity {

    public Button launchgoogleView;
    public Button saveplaceView;
    private Button autosave;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private float vitesse;
    private int flag_manuelle_save = 0;
    private int flag_autosave = 0;
    private TextView test;
    private int compteur;
    private Button fromwherelooking;
    private boolean a_pied=false;
    private boolean flag_tracking_fromwherelooking=false;

    private List<ParseObject> ob;
    private String s;
    private Date date;
    private ArrayList<String> listedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        launchgoogleView = (Button) findViewById(R.id.btn_launch_map);
        saveplaceView = (Button) findViewById(R.id.btn_saveplace);
        autosave = (Button) findViewById(R.id.btn_autosave);
        test = (TextView) findViewById(R.id.textView2);
       // fromwherelooking= (Button) findViewById(R.id.btn_showwherelooking);

        // Récupération base de données.
        date=new Date();


        //On indique qu'on recupère que les places libre marquée dans les 10 derniere minutes
        date.setMinutes(date.getMinutes()-10);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places_Libres");

        // Compare les 2 date et s'assurent qu'il y a moins de 10 entres chacune.
        query.whereGreaterThan("createdAt", date);

        try {
            ob = query.find();
            // Permet d'afficher la liste de dates correspondant au QUERY ci dessus
            for (int i = 0; i < ob.size(); i++) {
                //Attention ne pas oublier de changer le GMT
                SimpleTimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
                Format formatter = new SimpleDateFormat("HH:mm");
                s = formatter.format(ob.get(i).getCreatedAt());
                test.append("\n" + s );

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        //Lecture du tableau contenant les dates
        /*test.append("\n lecture tableau\n");
        for (int i=0; i < listedate.size(); i++) {
            test.append(listedate.get(i)+"\n");
        }*/




        // Lance le service de localisation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creation d'un listerner pour update la localisation
        locationListener = new LocationListener() {
            @Override
            //Fonction appelé quand il y a un changement de lattitude ou longitude.
            public void onLocationChanged(Location location) {
                //Stock la latitude et la longtitude
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                //////////////////////////////////////////////// AFFICHE DOU LES UTILISATEURS REGARDE LES PLACE ///////////////////////////////////////////////
                if (flag_tracking_fromwherelooking==true) {
                    Log.d("GOOGLEMAP", "okay ça marche");
                    // Montre d'où les utilisateurs regardent leur place
                    final ParseObject looking_from = new ParseObject("Looking_From_Where");
                    // On crée un Geopoint pour l'utiliser par la suite si nécessaire

                    ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
                    // On crée ces 2 colonnes pour facilité la récupération de ces 2 attributs
                    // Stock la latitude et la longitutde dans la base de données
                    looking_from.put("latitude", latitude);
                    looking_from.put("longitude", longitude);
                    looking_from.put("Location", point);
                    looking_from.saveInBackground();


                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();

                    //Arrete l'écoute si on enregistre manuellement la place
                    // Attention, ne pas oublier de remettre le flag à 0
                    flag_tracking_fromwherelooking = false;
                    locationManager.removeUpdates(locationListener);

                    //  ON LANCE L'ACTIVITE APRES AVOIR OBTENU LES INFO DE L'UTILISATEUR
                    launch_MapActivity();
                }

                ///////////////////////////////////// AJOUTE MANUELLEMENT UNE PLACE ////////////////////////////
                if (flag_manuelle_save == 1) {
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


                /////////////////////////////////////////// AJOUTE AUTOMATIQUEMENT UNE PLACE////////////////////////////////
                if (flag_autosave == 1) {

                    // Permet de récupérer la vitesse de l'utilisateur grâce au GPS
                    // ATTENTION LA VITESSE EST RECUPEREE EN METRE PAR SECONDE
                    vitesse = location.getSpeed();
                    // Donc je traduis la vitesse en km/h pour cela je multiplie par 3,6
                    vitesse = vitesse * (float) 3.6;

                    // On crée un compteur qui servira à l'ajoue de place de façon automatique.
                    // On incrémente un compteur si l'utilisateur est à l'arrêt
                    if (vitesse <= 1) {
                        compteur++;
                    } else { // On le réinitialise à 0 si l'utilisateur roule
                        compteur = 0;
                    }

                    // On choisit le compteur à partir duquel on enregistre la place, ici on choisira 15
                    if (compteur > 15) {
                        a_pied=true;
                        test.append("A PIED\n");
                    }

                    if (a_pied==true && vitesse > 1) {

                        //On enregistre la place
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
                        // On remet le compteur à 0 et on arrete le service
                        flag_autosave=0;
                        compteur=0;
                        a_pied=false;
                        locationManager.removeUpdates(locationListener);

                    }

                    test.append("Vitesse enregistré:" + vitesse + "compteur:" + compteur + "\n");
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
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }
            //return;
        } else {
            btn_sauvegardeplace();
            btn_automatique_save();
            btn_google();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    btn_sauvegardeplace();
                    btn_automatique_save();
                    btn_google();

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

    //////////////////////////////////GESTION DES BOUTTONS ///////////////////////

    public void launch_MapActivity() {
        startActivity(new Intent(this, MapsActivity.class));
    }


    // Affiche les places libres
    public void btn_google() {

        launchgoogleView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Chargement...", Toast.LENGTH_LONG).show();
                flag_tracking_fromwherelooking=true;
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });

    }

    // Sauvegarde une place libre grâce à la position actuelle
    public void btn_sauvegardeplace() {

        saveplaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_manuelle_save = 1;
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });
    }

    // Sauvegarde automatiquement une place grâce à la vitesse
    public void btn_automatique_save() {

        autosave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_autosave = 1;
                Toast.makeText(getApplicationContext(), "Activation du service d'ajout automatique de places", Toast.LENGTH_LONG).show();
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });

    }

    // Bouton pour afficher d'où les utilisateurs regardent les places
    public void btn_showing_from_where_users_looking_places(View v){
        // Message pour prévenir l'utilisateur
        Toast.makeText(getApplicationContext(), "Chargemeent des informations", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MapUserActivity.class));
    }

}
