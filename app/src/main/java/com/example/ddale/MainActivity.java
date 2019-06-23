package com.example.ddale;


import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *          avoir planté
     * Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
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

    /** Fonction onResume appelée lors de la reprise de l'activité courante après mise en pause
     *          pour cause d'appel à une autre activité
     * Permet d'activer/désactiver les boutons en fonction de l'état du réseau
     */
    @Override
    protected void onResume() {
        super.onResume();

        info.setEnabled(verifReseau());
        parcours.setEnabled(verifReseau());

        TextView internet = findViewById(R.id.internet);
        internet.setVisibility( (verifReseau()) ? View.INVISIBLE : View.VISIBLE);
    }

    /** Fonction qui permet de vérifier l'état du réseau WIFI
     * @return true si on active le bouton (on est bien connecté au réseau WIFI), false sinon
     */
    public boolean verifReseau()
    {
        // On vérifie si le réseau est disponible
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {

            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }

        Log.i("Réseau", "verifReseau: " + sType );
        return bStatut;
    }
}
