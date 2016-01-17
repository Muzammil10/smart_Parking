package org.smartparking.smartparking;

import android.app.Application;

import com.parse.Parse;

public class LauncherApplication extends Application {
    // L'objectif de l'application est d'initialiser Parse
    public void onCreate(){
        Parse.initialize(this);
    }
}
