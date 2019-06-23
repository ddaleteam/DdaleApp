package com.example.ddale;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * <b>Classe MainActivity</b>
 * Cette classe représente l'activité d'accueil de notre application
 *
 * @author ddaleteam
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Le bouton Information tableau
     */
    private Button info;
    /**
     * Le bouton Choix parcours
     */
    private Button parcours;
    private boolean monitoringConnectivity;
    private boolean estConnecte;
    private TextView internet;

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     *
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *                           avoir planté
     *                           Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, 1);
        // Initialisation des boutons
        info = findViewById(R.id.info);
        parcours = findViewById(R.id.parcours);

        /* Mise en place des écouteurs d'évènements */

        //Lors du clic sur le bouton info, on bascule sur l'activité QRActivity
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QRActivity.class);
                startActivity(i);
            }
        });

        parcours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChoixParcoursActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * Fonction onResume appelée lors de la reprise de l'activité courante après mise en pause
     * pour cause d'appel à une autre activité
     * Permet d'activer/désactiver les boutons en fonction de l'état du réseau
     */
    @Override
    protected void onResume() {
        super.onResume();
        verificationReseau();
        int visibiliteBoutons = estConnecte ? View.VISIBLE : View.GONE;
        info.setVisibility(visibiliteBoutons);
        parcours.setVisibility(visibiliteBoutons);

        internet = findViewById(R.id.internet);
        internet.setVisibility((estConnecte) ? View.GONE : View.VISIBLE);
    }

    /**
     * Permet de mettre en place le connectivity manager
     */
    private void verificationReseau() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // estConnecte is a boolean variable
        // here we check if network is connected or is getting connected
        estConnecte = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        // SHOW ANY ACTION YOU WANT TO SHOW
        // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK
        Log.i("PMR", " NO NETWORK");
        // if Network is not connected we will register a network callback to  monitor network
        connectivityManager.registerNetworkCallback(
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build(), connectivityCallback);
        monitoringConnectivity = true;


    }

    //Definition du callback du connnectivity manager
    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            estConnecte = true;
            Log.i("PMR", "INTERNET CONNECTED");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int visibiliteBoutons = View.VISIBLE;
                    info.setVisibility(visibiliteBoutons);
                    parcours.setVisibility(visibiliteBoutons);
                    internet.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onLost(Network network) {
            estConnecte = false;
            Log.i("PMR", "INTERNET LOST");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int visibiliteBoutons = View.GONE;
                    info.setVisibility(visibiliteBoutons);
                    parcours.setVisibility(visibiliteBoutons);
                    internet.setVisibility(View.VISIBLE);
                }
            });
        }
    };
}
