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
     * Constructeur par defaut
     * Toutes les chaines sont vides
     */
    public Calque() {
        urlCalque = "";
        description = "";
        urlAudio = "";
        typeCalque = "";
    }

    /**
     * Constructeur par parametres
     * @param description la description associee a l'anecdote
     * @param urlCalque l'url du calque
     * @param typeCalque le type du calque
     * @param urlAudio l'url du fichier audio associe au calque
     */
    public Calque(String description, String urlCalque, String typeCalque, String urlAudio) {
        this.description = description;
        this.urlCalque = urlCalque;
        this.typeCalque = typeCalque;
        this.urlAudio = urlAudio;
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
     * Mutateur de l'URL du calque
     * @param urlCalque la nouvelle URL
     */
    public void setUrlCalque(String urlCalque) {
        this.urlCalque = urlCalque;
    }

    /**
     * Accesseur du typeCalque
     * @return le typeCalque du calque
     */
    public String getTypeCalque() {
        return typeCalque;
    }

    /**
     * Mutateur du typeCalque du calque
     * @param typeCalque le nouveau typeCalque
     */
    public void setTypeCalque(String typeCalque) {
        this.typeCalque = typeCalque;
    }

    /**
     * Accesseur de la description
     * @return la description associee a l'anecdote
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutateur de la description associee a l'anecdote
     * @param description la nouvelle description a associer
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accesseur de l'URL du fichier audio
     * @return l'URL associee au fichier audio
     */
    public String getUrlAudio() {
        return urlAudio;
    }

    /**
     * Mutateur de l'URL du fichier audio
     * @param urlAudio la nouvelle URL a associer
     */
    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
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
                "}}";
    }
}
