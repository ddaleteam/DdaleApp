package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ddale.API.APIClient;
import com.example.ddale.API.APIInterface;
import com.example.ddale.modele.Parcours;

import java.util.ArrayList;

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
        
        // on crée l'adapter
        adapter = new RecyclerViewAdapter(ListeParcours, this);

        // on associe l'adapter au recyclerview
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        afficherParcours();
    }


    /**
     * Permet d'afficher le parcours
     */
    public void afficherParcours(){
        Log.d(TAG, "afficherParcours: ");
        // on crée le service de communication avec l'API
        APIInterface api = APIClient.createService(APIInterface.class);
        // on fait appel à l'API
        Call<ArrayList<Parcours>> call = api.appelAPIParcours();
        // le service va faire une async task automatiquement
        call.enqueue(new Callback<ArrayList<Parcours>>() {
            /**
             * La méthode appelée lors de la réponse à l'API
             * @param call l'appel à l'API
             * @param response la réponse de l'API
             */
            @Override
            public void onResponse(Call<ArrayList<Parcours>> call, Response<ArrayList<Parcours>> response) {
                if (response.isSuccessful()) { // dans le cas où l'API ne renvoie pas d'erreur
                    Log.d(TAG, "onResponse: " + response.code());
                    ListeParcours = response.body();
                    adapter.show(ListeParcours);
                } else { // dans le cas où l'API renvoie une erreur
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            /**
             * La méthode appelée lors de l'échec de l'envoi de la requête à l'API
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<ArrayList<Parcours>> call, Throwable t) {
                Log.d(TAG, "Erreur lors de l'appel à l'API pour récupérer l'oeuvre : timeout");
            }
        });
    }

    /**
     * La méthode appelée lors d'un clique sur le recyclerview
     * @param position la position de l'élément cliqué
     */
    @Override
    public void onParcoursClick(int position) {
        // on démarre MapActivity
        Intent intent = new Intent(this, MapActivity.class);

        intent.putExtra("parcoursId", ListeParcours.get(position).getId());

        this.startActivity(intent);

    }
}