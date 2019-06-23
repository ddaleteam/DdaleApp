package com.example.ddale.API;
import com.example.ddale.modele.Oeuvre;
import com.example.ddale.modele.Parcours;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface APIInterface {

    @GET("oeuvres/{id}")
    Call<Oeuvre> appelAPIOeuvre(@Path("id") int id);

    @GET("parcours")
    Call<ArrayList<Parcours>> appelAPIParcours();

    @GET("parcours/{id}")
    Call<Parcours> appelAPIParcoursID(@Path("id") int id);

}
