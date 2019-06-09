package com.example.ddale.modele;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>Classe Parcours</b>
 * Cette classe represente un parcours au sein du musee
 * Un parcours peut être thematique, ou associe a un auteur, une periode, ...
 *
 * @author ddaleteam
 * @version 1.0
 */
public class Parcours {

    /**
     * <b>id</b> represente l'identifiant unique du parcours
     * Cet identifiant est instancie par l'API
     */
    private int id;
    /**
     * <b>nom</b> represente le nom du parcours
     */
    private String nom;
    /**
     * <b>oeuvres</b> represente la liste des oeuvres
     */
    private List<Oeuvre> oeuvres;
    /**
     * <b>duree</b> represente la duree du parcours
     */
    private String duree;

    /**
     * Constructeur par defaut
     * Le point inferieur gauche correspond a l'origine du repere, la description est une chaine
     *      vide
     */
    public Parcours() {
        nom = "";
        oeuvres = new ArrayList<>();
        duree = "0";
    }

    /**
     * Constructeur par parametres
     * @param nom le nom du parcours
     * @param duree la duree du parcours
     * La liste des oeuvres associees au parcours est vide
     */
    public Parcours(String nom, String duree) {
        this.nom = nom;
        this.duree = duree;
        oeuvres = new ArrayList<>();
    }

    /**
     * Constructeur par parametres
     * @param nom le nom du parcours
     * @param oeuvres la liste des oeuvres associees au parcours
     * @param duree la duree du parcours
     */
    public Parcours(String nom, List<Oeuvre> oeuvres, String duree) {
        this.nom = nom;
        this.oeuvres = oeuvres;
        this.duree = duree;
    }

    /**
     * Accesseur de l'identifiant
     * @return l'identifiant de l'oeuvre
     */
    public int getId() {
        return id;
    }

    /**
     * Accesseur du nom
     * @return le nom du parcours
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mutateur du nom du parcours
     * @param nom le nouveau nom a attribuer
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Accesseur de la liste des oeuvres
     * @return la liste des oeuvres associees au parcours
     */
    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    /**
     * Mutateur de la liste des oeuvres
     * @param oeuvres la nouvelle liste a attribuer
     */
    public void setOeuvres(List<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
    }

    /**
     * Accesseur de la duree
     * @return la duree du parcours
     */
    public String getDuree() {
        return duree;
    }

    /**
     * Mutateur de la duree du parcours
     * @param duree la nouvelle a attribuer
     */
    public void setDuree(String duree) {
        this.duree = duree;
    }

    /**
     * Affichage d'un parcours
     * @return une chaine de caractere au format JSON representant un parcours
     */
    @Override
    public String toString() {
        String res = "{\"parcours\": {" +
                "\"id\": " + id +
                ", \"nom\": \"" + nom + '\"' +
                ", \"oeuvres\": [";
        Iterator<Oeuvre> iterator = oeuvres.iterator();
        while (iterator.hasNext()) {
            Oeuvre o = iterator.next();
            String afficheOeuvre = o.toString();
            afficheOeuvre = afficheOeuvre.substring(11);
            afficheOeuvre = afficheOeuvre.substring(0, (afficheOeuvre.length() - 1));
            afficheOeuvre += ", ";
            res += afficheOeuvre;
        }
        res = res.substring(0, (res.length() - 2)) + "]";
        res += ", \"duree\": \"" + duree + '\"' +
                "}}";
        return res;
    }

    /* Verification de l'affichage des methodes toString sous format JSON (penser a initialiser manuellement les identifiants !)
    public static void main(String[] args){
        Calque mAnecdote1 = new Calque("coucou", "cc", "anecdote");
        Calque mAnecdote2 = new Calque("salut", "yo", "composition");
        Oeuvre mOeuvre1 = new Oeuvre();
        Oeuvre mOeuvre2 = new Oeuvre();
        mOeuvre1.getCalques().add(mAnecdote1);
        mOeuvre2.getCalques().add(mAnecdote1);
        mOeuvre2.getCalques().add(mAnecdote2);
        Parcours mParcours = new Parcours();
        mParcours.getOeuvres().add(mOeuvre1);
        mParcours.getOeuvres().add(mOeuvre2);
        System.out.println(mAnecdote1);
        System.out.println(mOeuvre1);
        System.out.println(mParcours.toString());
    }
    */
}
