package fr.univrouen.cv24.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class Autre {

    @Field("titre")
    private String titre;

    @Field("comment")
    private String comment;

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
