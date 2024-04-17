package fr.univrouen.cv24.model;

public class Autre {

    private String titre;
    private String comment;

    public Autre(String titre, String comment) {
        this.titre = titre;
        this.comment = comment;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
