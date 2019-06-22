package com.example.ddale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <b>Classe MainActivity</b>
 * Cette classe représente l'activité d'accueil de notre application
 *
 * @author ddaleteam
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * <b>parcours</b> le bouton permettant de sélectionner le mode Parcours
     */
    private Button parcours;
    /**
     * <b>info</b> le bouton permettant de sélectionner le mode Informations
     */
    private Button info;

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

        /* Bouton de debug permettant d'accéder à l'activité ARActivity sans passer par l'activité
                QRActivity
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
