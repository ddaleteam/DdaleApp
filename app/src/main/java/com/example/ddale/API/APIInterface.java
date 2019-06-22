package com.example.ddale.API;
import com.example.ddale.modele.Oeuvre;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * <b>Interface APIInterface</b>
 * Cette interface regroupe les différentes requêtes soumises à l'API
 *
 * @author ddaleteam
 * @version 1.0
 */
public interface APIInterface {

    /**
     * Requête de récupération d'une oeuvre
     * @param id l'identifiant de l'oeuvre à récupérer
     * @return l'oeuvre demandée
     */
    @GET("oeuvres/{id}")
    Call<Oeuvre> appelAPIOeuvre(@Path("id") int id);

}
