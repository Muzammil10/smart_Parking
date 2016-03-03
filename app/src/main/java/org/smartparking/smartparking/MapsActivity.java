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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParseObject> ob;
    private double latitude;
    private double longitude;
    private Date oldplace;
    private Date newplace;
    private boolean vadisparaitre=false;

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

        // MARQUEUR POUR LES VIEILLES PLACES
        oldplace=new Date();

        //On indique qu'on recupère que les places libre marquée dans les 10 derniere minutes
        oldplace.setMinutes(oldplace.getMinutes()-10);
                // Récupération base de données.
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Places_Libres");
                 query.whereGreaterThan("createdAt", oldplace);

                try {
                    ob= query.find();
                    for (int i=0; i < ob.size(); i++) {

                        latitude=ob.get(i).getDouble("latitude");
                        longitude=ob.get(i).getDouble("longitude");

                        vadisparaitre= true;
                        createMarker(latitude, longitude, vadisparaitre).showInfoWindow();


                    }
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
        // MARQUEUR POUR LES nouvelles places
        newplace=new Date();

        //On indique qu'on recupère que les places libre marquée dans les 10 derniere minutes
        newplace.setMinutes(newplace.getMinutes() - 5);
        // Récupération base de données.
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Places_Libres");
        query2.whereGreaterThan("createdAt", newplace);

        try {
            ob= query2.find();
            for (int i=0; i < ob.size(); i++) {

                latitude=ob.get(i).getDouble("latitude");
                longitude=ob.get(i).getDouble("longitude");

                vadisparaitre= false;
                createMarker(latitude, longitude, vadisparaitre).showInfoWindow();


            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

    }
    public Marker createMarker(double latitude, double longitude, boolean disparition) {
        if (disparition==true) {
            return mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        } else {return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

    }
}
