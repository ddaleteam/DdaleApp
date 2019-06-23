package com.example.ddale;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Button info = findViewById(R.id.info);
        Button parcours = findViewById(R.id.parcours);

        /* Mise en place des écouteurs d'évènements */

        //Lors du clic sur le bouton info, on bascule sur l'activité QRActivity
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QRActivity.class);
                startActivity(i);
            }
        });

        /* Bouton de debug permettant d'accéder à l'activité ARActivity sans passer par l'activité
                QRActivity */
        /*
        Button btnDebugAR = findViewById(R.id.btnDebugAR);
        btnDebugAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent arIntent = new Intent(MainActivity.this, ARActivity.class);
                startActivity(arIntent);
            }
        });
        */
    }
}
