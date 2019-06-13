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
     * <b>annee</b> represente la annee de creation de l'oeuvre
     */
    @SerializedName("annee")
    private String annee;
    /**
     * <b>hauteur</b> represente la hauteur de l'oeuvre (pour un tableau)
     */
    @SerializedName("hauteur")
    private String hauteur;

    /**
     * <b>largeur</b> represente la largeur de l'oeuvre (pour un tableau)
     */
    @SerializedName("largeur")
    private String largeur;


    /**
     * <b>technique</b> represente la technique utilisee pour creer l'oeuvre
     */
    @SerializedName("technique")
    private String technique;
    /**
     * <b>urlCible</b> represente l'URL de l'image de l'oeuvre a reconnaitre
     */
    @SerializedName("urlCible")
    private String urlCible;
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
        annee = "";
        largeur = "";
        hauteur = "";
        technique = "";
        urlCible = "";
        calques = new ArrayList<>();
        audio = "";
        composition = "";
    }


    /**
     * Constructeur par parametres d'une oeuvre
     * @param position sa position
     * @param titre son titre
     * @param auteur son auteur
     * @param date sa annee de creation
     * @param hauteur la hauteur
     * @param largeur la largeur
     * @param technique la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     * La liste des calques est vide
     */
    public Oeuvre(int position, String titre, String auteur, String date, String hauteur, String largeur, String technique, String urlImageCible, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = date;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.technique = technique;
        this.urlCible = urlImageCible;
        this.audio = audio;
        this.composition = composition;
        calques = new ArrayList<>();
    }

    /**
     * Constructeur par parametres d'une oeuvre
     * @param position sa position
     * @param titre son titre
     * @param auteur son auteur
     * @param date sa annee de creation
     * @param hauteur la hauteur
     * @param largeur la largeur
     * @param technique la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param calques la liste des calques
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     */
    public Oeuvre(int position, String titre, String auteur, String date, String hauteur, String largeur, String technique, String urlImageCible, List<Calque> calques, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = date;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.technique = technique;
        this.urlCible = urlImageCible;
        this.calques = calques;
        this.audio = audio;
        this.composition = composition;
    }
    public Oeuvre( Oeuvre oeuvre){
        this.position = oeuvre.position;
        this.titre = oeuvre.titre;
        this.auteur = oeuvre.auteur;
        this.annee = oeuvre.annee;
        this.hauteur = oeuvre.hauteur;
        this.largeur = oeuvre.largeur;
        this.technique = oeuvre.technique;
        this.urlCible = oeuvre.urlCible;
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
     * Accesseur de la annee
     * @return la annee de creation de l'oeuvre
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Mutateur de la annee
     * @param annee la nouvelle annee de creation a attribuer
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * Accesseur da la hauteur
     * @return la hauteur de l'oeuvre
     */
    public String getHauteur() {
        return hauteur;
    }

    /**
     * Mutateur de la hauteur
     * @param hauteur la nouvelle hauteur à attribuer
     */
    public void setHauteur(String hauteur) {
        this.hauteur = hauteur;
    }

    /**
     * Accesseur de la hauteur
     * @return la hauteur de l'oeuvre
     */
    public String getLargeur() {
        return largeur;
    }

    /**
     * Mutateur de la largeur
     * @param largeur la nouvelle largeur à attribuer
     */
    public void setLargeur(String largeur) {
        this.largeur = largeur;
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
    public String getUrlCible() {
        return urlCible;
    }

    /**
     * Mutateur de l'URL de l'image de l'ouvre cible
     * @param urlCible la nouvelle URL a attribuer
     */
    public void setUrlCible(String urlCible) {
        this.urlCible = urlCible;
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
                ", \"annee\": \"" + annee + '\"' +
                ", \"hauteur\": \"" + hauteur + '\"' +
                ", \"largeur\": \"" + largeur + '\"' +
                ", \"technique\": \"" + technique + '\"' +
                ", \"urlCible\": \"" + urlCible + '\"' +
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
