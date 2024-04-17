package fr.univrouen.cv24.model;

import org.springframework.data.mongodb.core.mapping.Language;

import java.util.List;

public class Divers {

    private List<Langue> lv;

    private List<Autre> autre;

    public Divers(List<Langue> lv, List<Autre> autre) {
        this.lv = lv;
        this.autre = autre;
    }

    public List<Langue> getLv() {
        return lv;
    }

    public void setLv(List<Langue> lv) {
        this.lv = lv;
    }

    public List<Autre> getAutre() {
        return autre;
    }

    public void setAutre(List<Autre> autre) {
        this.autre = autre;
    }
}
