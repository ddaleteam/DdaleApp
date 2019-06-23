package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.ddale.modele.Parcours;

import java.util.ArrayList;
import java.util.List;

public class ChoixParcoursActivity extends AppCompatActivity implements RecyclerViewAdapter.OnParcoursListener {

    ArrayList<Parcours> ListeParcours = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_parcours);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<Parcours> ListeParcours = creerListeParcours();

        adapter = new RecyclerViewAdapter(ListeParcours, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    private ArrayList<Parcours> creerListeParcours() {
        Parcours parcours1 = new Parcours("Premier parcours", 10);
        Parcours parcours2 = new Parcours("Deuxième parcours", 30);
        Parcours parcours3 = new Parcours("Premier parcours", 20);


        ListeParcours.add(parcours1);
        ListeParcours.add(parcours2);
        ListeParcours.add(parcours3);

        return ListeParcours;
    }

    @Override
    public void onParcoursClick(int position) {
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra("IdParcours", ListeParcours.get(position).getId());

        this.startActivity(intent);

    }
}