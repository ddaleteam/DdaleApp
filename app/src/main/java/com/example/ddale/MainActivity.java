package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button parcours;
    private Button info;
    private String CAT="MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des boutons
        info = findViewById(R.id.info);
        parcours = findViewById(R.id.parcours);

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
}
