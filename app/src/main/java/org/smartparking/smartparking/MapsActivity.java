package org.smartparking.smartparking;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParseObject> ob;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng local_placelibre;

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Paramètrage de la carte (avec les boutons zoom, localisation ...)
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);


        // Affiche la position sur la carte (ATTENTION PERMISSION)
        //mMap.setMyLocationEnabled(true);

        ///// Il faut afficher toutes les places libre, et mettre un marqueur dessus : donc lecture bdd.


                // Récupération base de données.
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Places_Libres");
                // limite à 1 résultat
               // query.setLimit(1);
                try {
                    ob= query.find();
                    for (int i=0; i < ob.size(); i++) {

                        latitude=ob.get(i).getDouble("latitude");
                        longitude=ob.get(i).getDouble("longitude");

                        createMarker(latitude, longitude).showInfoWindow();


                    }
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }

        // Permet d'afficher un marker
        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }
    public Marker createMarker(double latitude, double longitude) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                );
    }
}
