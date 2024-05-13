package fr.univrouen.cv24.model;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Field;

public class Identite {

    @Field("cv24:genre")
    private String genre;

    @Field("cv24:nom")
    private String nom;

    @Field("cv24:prenom")
    private String prenom;

    @Field("cv24:tel")
    private String tel;

    @Field("cv24:mel")
    private String mel;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String  genre) {
        this.genre = genre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMel() {
        return mel;
    }

    public void setMel(String mel) {
        this.mel = mel;
    }

}
