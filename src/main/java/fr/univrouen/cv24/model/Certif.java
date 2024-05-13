package fr.univrouen.cv24.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Certif {

    @Field("cv24:datedeb")
    private Date datedeb;

    @Field("cv24:datefin")
    private Date datefin;

    @Field("cv24:titre")
    private String titre;

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
