package fr.univrouen.cv24.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CV24")
public class CV24 {

    @Id
    private Integer id;
    private Identite identite;

    private Objectif objectif;

    private Prof prof;

    private Competences competences;

    private Divers divers;

    @PersistenceCreator
    public CV24(Integer id,
                Identite identite,
                Objectif objectif,
                Prof prof,
                Competences competences,
                Divers divers) {
        this.id = id;
        this.identite = identite;
        this.objectif = objectif;
        this.prof = prof;
        this.competences = competences;
        this.divers = divers;
    }

    public Integer getId() {
        return id;
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
