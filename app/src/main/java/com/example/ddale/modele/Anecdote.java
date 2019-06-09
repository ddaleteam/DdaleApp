package com.example.ddale.modele;

/**
 * <b>Classe Anecdote</b>
 * Cette classe represente une anecdote
 * Une anecdote est une zone rectangulaire au sein d'une oeuvre d'art
 * On repere une anecdote par les coordonnees du point inferieur gauche de la zone rectangulaire
 * L'origine du repere est associee au point inferieur gauche de l'oeuvre d'art
 *
 * @author ddaleteam
 * @version 1.0
 */
public class Anecdote {

    /**
     * <b>id</b> represente l'identifiant unique de l'anecdote
     * Cet identifiant est instancie par l'API
     */
    private int id;
    /**
     * <b>abscisse</b> represente l'abscisse du point inferieur gauche
     */
    private double abscisse;
    /**
     * <b>ordonnee</b> represente l'ordonnee du point inferieur gauche
     */
    private double ordonnee;
    /**
     * <b>description</b> represente le texte associe a l'anecdote
     */
    private String description;

    /**
     * Constructeur par defaut
     * Le point inferieur gauche correspond a l'origine du repere, la description est une chaine
     *      vide
     */
    public Anecdote() {
        abscisse = 0;
        ordonnee = 0;
        description = "";
    }

    /**
     * Constructeur par parametres
     * @param abscisse l'abscisse du point inferieur gauche
     * @param ordonnee l'ordonnee du point inferieur gauche
     * @param description la description associee a l'anecdote
     */
    public Anecdote(double abscisse, double ordonnee, String description) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.description = description;
    }

    /**
     * Accesseur de l'identifiant
     * @return l'identifiant de l'anecdote
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur de l'abscisse
     * @return l'abscisse du point inferieur gauche
     */
    public double getAbscisse() {
        return abscisse;
    }

    /**
     * Mutateur de l'abscisse du point inferieur gauche
     * @param abscisse la nouvelle abscisse a attribuer
     */
    public void setAbscisse(double abscisse) {
        this.abscisse = abscisse;
    }

    /**
     * Accesseur de l'ordonnee
     * @return l'ordonnee du point inferieur gauche
     */
    public double getOrdonnee() {
        return ordonnee;
    }

    /**
     * Mutateur de l'ordonnee du point inferieur gauche
     * @param ordonnee la nouvelle ordonnee a attribuer
     */
    public void setOrdonnee(double ordonnee) {
        this.ordonnee = ordonnee;
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
     * @param description la nouvelle descritpion a associer
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Affichage d'une anecdote
     * @return une chaine de caractere au format JSON representant une anecdote
     */
    @Override
    public String toString() {
        return "{\"anecdote\": {" +
                "\"id\": " + id +
                ", \"abscisse\": " + abscisse +
                ", \"ordonnee\": " + ordonnee +
                ", \"description\": \"" + description + "\"" +
                "}}";
    }
}
