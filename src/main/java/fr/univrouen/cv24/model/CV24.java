package fr.univrouen.cv24.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CV24")
public class CV24 {

    @Id
    @Field("_id")
    private String id;

    @Field("id")
    private int cvId;

    @DBRef(lazy = true)
    @Field("cv24:cv24.cv24:identite")
    private Identite identite;

    @DBRef(lazy = true)
    @Field("cv24:cv24.cv24:objectif")
    private Objectif objectif;

    @DBRef(lazy = true)
    @Field("cv24:cv24.cv24:prof")
    private Prof prof;

    @DBRef(lazy = true)
    @Field("cv24:cv24.cv24:competences")
    private Competences competences;

    @DBRef(lazy = true)
    @Field("cv24:cv24.cv24:divers")
    private Divers divers;

    public String  getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public int getCvId() {
        return cvId;
    }

    public void setCvId(int cvId) {
        this.cvId = cvId;
    }


    public Identite getIdentity() {
        return identite;
    }

    public void setIdentity(Identite identity) {
        this.identite = identity;
    }

    public Objectif getObjectif() {
        return objectif;
    }

    public void setObjectif(Objectif objectif) {
        this.objectif = objectif;
    }

    public Prof getProf() {
        return prof;
    }

    public void setProf(Prof prof) {
        this.prof = prof;
    }


    public Competences getCompetences() {
        return competences;
    }

    public void setCompetences(Competences competences) {
        this.competences = competences;
    }
    public Divers getDivers() {
        return divers;
    }

    public void setDivers(Divers divers) {
        this.divers = divers;
    }
}
