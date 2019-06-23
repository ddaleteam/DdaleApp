package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ddale.API.APIClient;
import com.example.ddale.API.APIInterface;
import com.example.ddale.modele.Oeuvre;
import com.example.ddale.modele.Parcours;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixParcoursActivity extends AppCompatActivity implements RecyclerViewAdapter.OnParcoursListener {

    private static final String TAG = "ChoixParcoursActivity";
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

        afficherParcours();
    }
    

    public void afficherParcours(){
        Log.d(TAG, "afficherParcours: ");
        APIInterface api = APIClient.createService(APIInterface.class);
        Call<ArrayList<Parcours>> call = api.appelAPIParcours();
        call.enqueue(new Callback<ArrayList<Parcours>>() {
            @Override
            public void onResponse(Call<ArrayList<Parcours>> call, Response<ArrayList<Parcours>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                    ListeParcours = response.body();
                    adapter.show(ListeParcours);
                } else {
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Parcours>> call, Throwable t) {
                Log.d(TAG, "Erreur lors de l'appel à l'API pour récupérer l'oeuvre : timeout");
            }
        });
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

        intent.putExtra("parcoursId", ListeParcours.get(position).getId());

        this.startActivity(intent);

    }
}