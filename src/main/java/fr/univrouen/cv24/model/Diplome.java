package fr.univrouen.cv24.model;

import java.util.Date;
import java.util.List;

public class Diplome {

    private int niveau;

    private Date date;
    private String institut;
    private List<String> titres;

    public Diplome(int niveau, Date date, String institut, List<String> titres) {
        this.niveau = niveau;
        this.date = date;
        this.institut = institut;
        this.titres = titres;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInstitut() {
        return institut;
    }

    public void setInstitut(String institut) {
        this.institut = institut;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
    }
}
