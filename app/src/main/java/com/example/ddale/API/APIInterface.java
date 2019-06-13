package com.example.ddale.API;
import com.example.ddale.modele.Oeuvre;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface APIInterface {

    @GET("oeuvres/{id}")
    Call<Oeuvre> appelAPIOeuvre(@Path("id") int id);

}
