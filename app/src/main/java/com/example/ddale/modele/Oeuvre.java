package com.example.ddale.modele;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

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
    private int annee;
    /**
     * <b>hauteur</b> represente la hauteur de l'oeuvre (pour un tableau)
     */
    @SerializedName("hauteur")
    private int hauteur;
    /**
     * <b>largeur</b> represente la largeur de l'oeuvre (pour un tableau)
     */
    @SerializedName("largeur")
    private int largeur;
    /**
     * <b>technique</b> represente la technique utilisee pour creer l'oeuvre
     */
    @SerializedName("technique")
    private String technique;
    /**
     * <b>urlCible</b> represente l'URL de l'image de l'oeuvre a reconnaitre
     */
    @SerializedName("urlCible")
    private String urlImageCible;
    /**
     * <b>calques</b> represente la liste des calques associees a l'oeuvre
     */
    @SerializedName("calques")
    private List<Calque> calques;
    /**
     * <b>audio</b> contient une URL dirigeant vers le fichier audio associe a l'oeuvre
     */
    @SerializedName("urlAudio")
    private String urlAudio;
    /**
     * <b>localisation</b> contient l'emplacement de l'oeuvre.
     */
    private GeoPoint localisation;

    /**
     * <b>latitude</b> contient la latitude de l'oeuvre
     */
    @SerializedName("latitude")
    private double latitude;

    /**
     * <b>longitude</b> contient la longitude de l'oeuvre
     */
    @SerializedName("longitude")
    private double longitude;

    /**
     * <b>altitude</b> contient la altitude de l'oeuvre
     */
    @SerializedName("altitude")
    private double altitude;

    /**
     * Constructeur par parametres d'une oeuvre
     *
     * @param longitude     de l'oeuvre
     * @param latitude      de l'oeuvre
     * @param altitude      de l'oeuvre
     * @param titre         son titre
     * @param auteur        son auteur
     * @param annee         son annee de creation
     * @param hauteur       la hauteur
     * @param largeur       la largeur
     * @param technique     la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param urlAudio      le lien vers le fichier audio
     *                      La liste des calques est vide
     */
    public Oeuvre(int id, double longitude, double latitude, double altitude, String titre, String auteur, int annee, int hauteur, int largeur, String technique, String urlImageCible, String urlAudio) {
        this.id = id;
        this.localisation = new GeoPoint(latitude, longitude, altitude);
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.technique = technique;
        this.urlImageCible = urlImageCible;
        this.urlAudio = urlAudio;
        this.calques = new ArrayList<>();
    }

    /**
     * Constructeur par parametres d'une oeuvre
     *
     * @param longitude     de l'oeuvre
     * @param latitude      de l'oeuvre
     * @param altitude      de l'oeuvre
     * @param titre         son titre
     * @param auteur        son auteur
     * @param annee         sa annee de creation
     * @param hauteur       la hauteur
     * @param largeur       la largeur
     * @param technique     la technique utilisee
     * @param urlImageCible l'URL de l'image de l'oeuvre cible
     * @param calques       la liste des calques
     * @param urlAudio      le lien vers le fichier audio
     */
    public Oeuvre(int id, int position, double longitude, double latitude, double altitude, String titre, String auteur, int annee, int hauteur, int largeur, String technique, String urlImageCible, List<Calque> calques, String urlAudio) {
        this.id = id;
        this.localisation = new GeoPoint(latitude, longitude, altitude);
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.technique = technique;
        this.urlImageCible = urlImageCible;
        this.calques = calques;
        this.urlAudio = urlAudio;
    }

    /**
     * Constructeur par copie d'une oeuvre
     *
     * @param oeuvre l'oeuvre a copier
     */
    public Oeuvre(Oeuvre oeuvre) {
        this.id = oeuvre.id;
        this.localisation = oeuvre.localisation;
        this.titre = oeuvre.titre;
        this.auteur = oeuvre.auteur;
        this.annee = oeuvre.annee;
        this.hauteur = oeuvre.hauteur;
        this.largeur = oeuvre.largeur;
        this.technique = oeuvre.technique;
        this.urlImageCible = oeuvre.urlImageCible;
        this.calques = oeuvre.calques;
        this.urlAudio = oeuvre.urlAudio;
    }

    /**
     * Accesseur de l'identifiant
     *
     * @return l'identifiant de l'oeuvre
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur de la localisation
     *
     * @return la localisation de l'oeuvre
     */
    public GeoPoint getLocalisation() {
        if (localisation!=null){
            return localisation;
        }
        else {
            localisation = new GeoPoint(latitude, longitude, altitude);
            return localisation;
        }
    }

    /**
     * Accesseur du titre
     *
     * @return le titre de l'oeuvre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Accesseur de l'auteur
     *
     * @return l'auteur de l'oeuvre
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Accesseur de la annee
     *
     * @return la annee de creation de l'oeuvre
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * Accesseur da la hauteur
     *
     * @return la hauteur de l'oeuvre
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * Accesseur de la hauteur
     *
     * @return la hauteur de l'oeuvre
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * Accesseur de la technique
     *
     * @return la technique utilisee pour creer l'oeuvre
     */
    public String getTechnique() {
        return technique;
    }

    /**
     * Accesseur de l'URL
     *
     * @return l'URL de l'image de l'oeuvre cible
     */
    public String getUrlImageCible() {
        return urlImageCible;
    }

    /**
     * Accesseur de la liste des calques
     *
     * @return la liste des calques associees a l'oeuvre
     */
    public List<Calque> getCalques() {
        return calques;
    }

    /**
     * Accesseur de l'URL du fichier audio
     *
     * @return l'URL du fichier audio associe a l'oeuvre
     */
    public String getUrlAudio() {
        return urlAudio;
    }

    /**
     * Affichage d'une oeuvre
     *
     * @return une chaine de caractere au format JSON representant une oeuvre
     */
    @Override
    public String toString() {
        String res = "{\"oeuvre\": {" +
                "\"id\": " + id +
                ", \"titre\": \"" + titre + '\"' +
                ", \"auteur\": \"" + auteur + '\"' +
                ", \"annee\": \"" + annee + '\"' +
                ", \"hauteur\": \"" + hauteur + '\"' +
                ", \"largeur\": \"" + largeur + '\"' +
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
        res += ", \"urlAudio\": \"" + urlAudio + '\"' +
                "}}";
        return res;
    }
}
