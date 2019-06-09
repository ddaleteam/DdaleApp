package com.example.ddale.modele;

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
    private int id;
    /**
     * <b>position</b> represente la position de l'oeuvre dans le musee
     */
    private int position;
    /**
     * <b>titre</b> represente le titre de l'oeuvre
     */
    private String titre;
    /**
     * <b>auteur</b> represente l'auteur de l'oeuvre
     */
    private String auteur;
    /**
     * <b>date</b> represente la date de creation de l'oeuvre
     */
    private String date;
    /**
     * <b>dimensions</b> represente les dimensions de l'oeuvre (hauteur x largeur pour un tableau)
     */
    private String dimensions;
    /**
     * <b>technique</b> represente la technique utilisee pour creer l'oeuvre
     */
    private String technique;
    /**
     * <b>anecdotes</b> represente la liste des anecdotes associees a l'oeuvre
     */
    private List<Anecdote> anecdotes;
    /**
     * <b>audio</b> contient une URL dirigeant vers le fichier audio associe a l'oeuvre
     */
    private String audio;
    /**
     * <b>composition</b> contient une URL dirigeant vers les fichiers images associes a l'oeuvre
     * Ces images sont superposees a l'oeuvre lors de l'affichage (realite augmentee)
     */
    private String composition;

    /**
     * Constructeur par defaut
     * La liste des anecdotes est vide, la position de l'oeuvre est 0, tous les autres champs sont
     *      des chaines vides
     */
    public Oeuvre() {
        position = 0;
        titre = "";
        auteur = "";
        date = "";
        dimensions = "";
        technique = "";
        anecdotes = new ArrayList<>();
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
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     * La liste des anecdotes est vide
     */
    public Oeuvre(int position, String titre, String auteur, String date, String dimensions, String technique, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.dimensions = dimensions;
        this.technique = technique;
        this.audio = audio;
        this.composition = composition;
        anecdotes = new ArrayList<>();
    }

    /**
     * Constructeur par parametres d'une oeuvre
     * @param position sa position
     * @param titre son titre
     * @param auteur son auteur
     * @param date sa date de creation
     * @param dimensions ses dimensions
     * @param technique la technique utilisee
     * @param anecdotes la liste des anecdotes
     * @param audio le lien vers le fichier audio
     * @param composition le lien vers les fichiers images
     */
    public Oeuvre(int position, String titre, String auteur, String date, String dimensions, String technique, List<Anecdote> anecdotes, String audio, String composition) {
        this.position = position;
        this.titre = titre;
        this.auteur = auteur;
        this.date = date;
        this.dimensions = dimensions;
        this.technique = technique;
        this.anecdotes = anecdotes;
        this.audio = audio;
        this.composition = composition;
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
     * Accesseur de la liste des anecdotes
     * @return la liste des anecdotes associees a l'oeuvre
     */
    public List<Anecdote> getAnecdotes() {
        return anecdotes;
    }

    /**
     * Mutateur de la liste des anecdotes
     * @param anecdotes la nouvelle liste a attribuer
     */
    public void setAnecdotes(List<Anecdote> anecdotes) {
        this.anecdotes = anecdotes;
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
                ", \"anecdotes\": [";
        Iterator<Anecdote> iterator = anecdotes.iterator();
        while (iterator.hasNext()) {
            Anecdote a = iterator.next();
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
