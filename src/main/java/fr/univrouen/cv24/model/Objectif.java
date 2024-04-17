package fr.univrouen.cv24.model;

public class Objectif {

    private String objectif;
    private String statut;

    public Objectif(String objectif, String statut) {
        this.objectif = objectif;
        this.statut = statut;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
