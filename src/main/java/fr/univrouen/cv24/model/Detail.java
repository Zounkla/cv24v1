package fr.univrouen.cv24.model;

import java.util.Date;

public class Detail {

    private Date datedeb;
    private Date datefin;
    private String titre;

    public Detail(Date datedeb, Date datefin, String titre) {
        this.datedeb = datedeb;
        this.datefin = datefin;
        this.titre = titre;
    }

    public Date getDatedeb() {
        return datedeb;
    }

    public void setDatedeb(Date datedeb) {
        this.datedeb = datedeb;
    }

    public Date getDatefin() {
        return datefin;
    }

    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
