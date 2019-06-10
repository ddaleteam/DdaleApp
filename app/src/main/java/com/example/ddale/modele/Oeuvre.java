package com.example.ddale.modele;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>Classe Oeuvre</b>
 * Cette classe represente une oeuvre d'art
 *
 * @author ddaleteam
 * @version 1.0
 */
public class Oeuvre {

    /**
     * <b>id</b> represente l'identifiant unique de l'oeuvre
     * Cet identifiant est instancie par l'API
     */
    @SerializedName("id")
    private int id;
    /**
     * <b>position</b> represente la position de l'oeuvre dans le musee
     */
    @SerializedName("position")
    private int position;
    /**
     * <b>titre</b> represente le titre de l'oeuvre
     */
    @SerializedName("titre")
    private String titre;
    /**
     * <b>auteur</b> represente l'auteur de l'oeuvre
     */
    @SerializedName("auteur")
    private String auteur;
    /**
     * <b>date</b> represente la date de creation de l'oeuvre
     */
    @SerializedName("date")
    private String date;
    /**
     * <b>dimensions</b> represente les dimensions de l'oeuvre (hauteur x largeur pour un tableau)
     */
    @SerializedName("dimensions")
    private String dimensions;
    /**
     * <b>technique</b> represente la technique utilisee pour creer l'oeuvre
     */
    @SerializedName("technique")
    private String technique;
    /**
     * <b>urlImageCible</b> represente l'URL de l'image de l'oeuvre a reconnaitre
     */
    @SerializedName("urlImageCible")
    private String urlImageCible;
    /**
     * <b>calques</b> represente la liste des calques associees a l'oeuvre
     */
    @SerializedName("calques")
    private List<Calque> calques;
    /**
     * <b>audio</b> contient une URL dirigeant vers le fichier audio associe a l'oeuvre
     */
    @SerializedName("audio")
    private String audio;
    /**
     * <b>composition</b> contient une URL dirigeant vers les fichiers images associes a l'oeuvre
     * Ces images sont superposees a l'oeuvre lors de l'affichage (realite augmentee)
     */
    @SerializedName("composition")
    private String composition;

    /**
     * Constructeur par defaut
     * La liste des calques est vide, la position de l'oeuvre est 0, tous les autres champs sont
     *      des chaines vides
     */
    public Oeuvre() {
        position = 0;
        titre = "";
        auteur = "";
        date = "";
        dimensions = "";
        technique = "";
        urlImageCible = "";
        calques = new ArrayList<>();
        audio = "";
        composition = "";
    }


    /**
     * Constructeur par parametres d'une oeuvre
     * @param position sa position
     * @param titre son titre
     * @param auteur son auteur
     * @param date sa date de creation
     * @param dimensions ses dimensions
     * @param technique la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     * La liste des calques est vide
     */
    public Oeuvre(int position, String titre, String auteur, String date, String dimensions, String technique, String urlImageCible, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.dimensions = dimensions;
        this.technique = technique;
        this.urlImageCible = urlImageCible;
        this.audio = audio;
        this.composition = composition;
        calques = new ArrayList<>();
    }

    /**
     * Constructeur par parametres d'une oeuvre
     * @param position sa position
     * @param titre son titre
     * @param auteur son auteur
     * @param date sa date de creation
     * @param dimensions ses dimensions
     * @param technique la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param calques la liste des calques
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     */
    public Oeuvre(int position, String titre, String auteur, String date, String dimensions, String technique, String urlImageCible, List<Calque> calques, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.dimensions = dimensions;
        this.technique = technique;
        this.urlImageCible = urlImageCible;
        this.calques = calques;
        this.audio = audio;
        this.composition = composition;
    }
    public Oeuvre( Oeuvre oeuvre){
        this.position = oeuvre.position;
        this.titre = oeuvre.titre;
        this.auteur = oeuvre.auteur;
        this.date = oeuvre.date;
        this.dimensions = oeuvre.dimensions;
        this.technique = oeuvre.technique;
        this.urlImageCible = oeuvre.urlImageCible;
        this.calques = oeuvre.calques;
        this.audio = oeuvre.audio;
        this.composition = oeuvre.composition;
    }

    /**
     * Accesseur de l'identifiant
     * @return l'identifiant de l'oeuvre
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur de la position
     * @return la position de l'oeuvre
     */
    public int getPosition() {
        return position;
    }

    /**
     * Mutateur de la position de l'oeuvre
     * @param position la nouvelle position a attribuer
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Accesseur du titre
     * @return le titre de l'oeuvre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Mutateur du titre
     * @param titre le nouveau titre a attribuer
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Accesseur de l'auteur
     * @return l'auteur de l'oeuvre
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Mutateur de l'auteur
     * @param auteur le nouvel auteur a attribuer
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Accesseur de la date
     * @return la date de creation de l'oeuvre
     */
    public String getDate() {
        return date;
    }

    /**
     * Mutateur de la date
     * @param date la nouvelle date de creation a attribuer
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Accesseur des dimensions
     * @return les dimensions de l'oeuvre
     */
    public String getDimensions() {
        return dimensions;
    }

    /**
     * Mutateur des dimensions
     * @param dimensions les nouvelles dimensions a attribuer
     */
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Accesseur de la technique
     * @return la technique utilisee pour creer l'oeuvre
     */
    public String getTechnique() {
        return technique;
    }

    /**
     * Mutateur de la technique
     * @param technique la nouvelle technique a attribuer
     */
    public void setTechnique(String technique) {
        this.technique = technique;
    }

    /**
     * Accesseur de l'URL
     * @return l'URL de l'image de l'oeuvre cible
     */
    public String getUrlImageCible() {
        return urlImageCible;
    }

    /**
     * Mutateur de l'URL de l'image de l'ouvre cible
     * @param urlImageCible la nouvelle URL a attribuer
     */
    public void setUrlImageCible(String urlImageCible) {
        this.urlImageCible = urlImageCible;
    }

    /**
     * Accesseur de la liste des calques
     * @return la liste des calques associees a l'oeuvre
     */
    public List<Calque> getCalques() {
        return calques;
    }

    /**
     * Mutateur de la liste des calques
     * @param calques la nouvelle liste a attribuer
     */
    public void setCalques(List<Calque> calques) {
        this.calques = calques;
    }

    /**
     * Accesseur de l'URL du fichier audio
     * @return l'URL du fichier audio associe a l'oeuvre
     */
    public String getAudio() {
        return audio;
    }

    /**
     * Mutateur de l'URL du fichier audio
     * @param audio la nouvelle URL a attribuer
     */
    public void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * Accesseur de l'URL des fichiers images
     * @return l'URL des fichiers images associes a l'oeuvre
     */
    public String getComposition() {
        return composition;
    }

    /**
     * Mutateur de l'URL des fichiers images
     * @param composition la nouvelle URL a attribuer
     */
    public void setComposition(String composition) {
        this.composition = composition;
    }

    /**
     * Affichage d'une oeuvre
     * @return une chaine de caractere au format JSON representant une oeuvre
     */
    @Override
    public String toString() {
        String res = "{\"oeuvre\": {" +
                "\"id\": " + id +
                ", \"position\": \"" + position + '\"' +
                ", \"titre\": \"" + titre + '\"' +
                ", \"auteur\": \"" + auteur + '\"' +
                ", \"date\": \"" + date + '\"' +
                ", \"dimensions\": \"" + dimensions + '\"' +
                ", \"technique\": \"" + technique + '\"' +
                ", \"urlImageCible\": \"" + urlImageCible + '\"' +
                ", \"calques\": [";
        Iterator<Calque> iterator = calques.iterator();
        while (iterator.hasNext()) {
            Calque a = iterator.next();
            String afficheAnecdote = a.toString();
            afficheAnecdote = afficheAnecdote.substring(13);
            afficheAnecdote = afficheAnecdote.substring(0, (afficheAnecdote.length() - 1));
            afficheAnecdote += ", ";
            res += afficheAnecdote;
        }
        res = res.substring(0, (res.length() - 2)) + "]";
        res += ", \"audio\": \"" + audio + '\"' +
                ", \"composition\": \"" + composition + '\"' +
                "}}";
        return res;
    }
}
