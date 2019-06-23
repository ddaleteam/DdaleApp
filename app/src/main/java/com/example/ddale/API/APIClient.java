package com.example.ddale.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <b>Classe APIClient</b>
 * Cette classe permet de fournir une interface de connexion à l'API
 *
 * @author ddaleteam
 * @version 1.0
 */
public class APIClient {
    /**
     * <b>BASE_URL</b> l'URL de base dirigeant vers notre API
     */
    public static String BASE_URL = "https://ddale.rezoleo.fr";

    /**
     * <b>retrofit</b> notre objet Retrofit contenant les informations nécessaires à la connexion
     */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * Initialisation de l'interface de connexion
     */
    public static <T> T createService (Class<T> type){
        return retrofit.create(type);
    }
}
