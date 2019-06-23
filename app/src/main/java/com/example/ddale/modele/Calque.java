package com.example.ddale.modele;

import com.google.gson.annotations.SerializedName;

/**
 * <b>Classe Calque</b>
 * Cette classe represente un calque
 * Un calque peut etre de typeCalque 'anecdote' ou bien 'composition'
 *
 * @author ddaleteam
 * @version 1.0
 */
public class Calque {

    /**
     * <b>id</b> represente l'identifiant unique du calque
     * Cet identifiant est instancie par l'API
     */
    @SerializedName("id")
    private int id;
    /**
     * <b>description</b> represente le texte associe au calque
     */
    @SerializedName("description")
    private String description;
    /**
     * <b>urlCalque</b> represente le texte associe au calque
     */
    @SerializedName("urlCalque")
    private String urlCalque;
    /**
     * <b>typeCalque</b> represente le typeCalque du calque
     */
    @SerializedName("typeCalque")
    private String typeCalque;
    /**
     * <b>urlAudio</b> contient une URL dirigeant vers le fichier audio associe au calque
     */
    @SerializedName("urlAudio")
    private String urlAudio;
    /**
     * <b>oeuvre_id</b> represente l'identifiant de l'oeuvre associee au calque
     */
    @SerializedName("oeuvre_id")
    private int oeuvre_id;


    /**
     * Constructeur par parametres
     * @param id l'identifiant du calque
     * @param description la description associee a l'anecdote
     * @param urlCalque l'url du calque
     * @param typeCalque le type du calque
     * @param urlAudio l'url du fichier audio associe au calque
     * @param oeuvre_id l'identifiant de l'oeuvre associee au calque
     */
    public Calque(int id, String description, String urlCalque, String typeCalque, String urlAudio,
                  int oeuvre_id) {
        this.id = id;
        this.description = description;
        this.urlCalque = urlCalque;
        this.typeCalque = typeCalque;
        this.urlAudio = urlAudio;
        this.oeuvre_id = oeuvre_id;
    }

    /**
     * Accesseur de l'identifiant
     * @return l'identifiant de l'anecdote
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur de l'URL
     * @return l'URL du calque
     */
    public String getUrlCalque() {
        return urlCalque;
    }

    /**
     * Accesseur du typeCalque
     * @return le typeCalque du calque
     */
    public String getTypeCalque() {
        return typeCalque;
    }

    /**
     * Accesseur de la description
     * @return la description associee a l'anecdote
     */
    public String getDescription() {
        return description;
    }

    /**
     * Accesseur de l'URL du fichier audio
     * @return l'URL associee au fichier audio
     */
    public String getUrlAudio() {
        return urlAudio;
    }

    /**
     * Accesseur de l'identifiant de l'oeuvre associee au calque
     * @return l'identifiant de l'oeuvre
     */
    public int getOeuvre_id() {
        return oeuvre_id;
    }

    /**
     * Affichage d'une anecdote
     * @return une chaine de caractere au format JSON representant une anecdote
     */
    @Override
    public String toString() {
        return "{\"anecdote\": {" +
                "\"id\": " + id +
                ", \"typeCalque\": " + typeCalque +
                ", \"urlCalque\": " + urlCalque +
                ", \"urlAudio\": " + urlAudio +
                ", \"description\": \"" + description + "\"" +
                ", \"oeuvre_id\": \"" + oeuvre_id + "\"" +
                "}}";
    }
}
