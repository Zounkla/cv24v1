package fr.univrouen.cv24.model;

import jakarta.xml.bind.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "CV")
public class TestCV implements Serializable {
    private static final long serialVersionUID = 2024L;
    private static int compteur = 1;

    @Id
    private Integer id;

    @Field(value="name")
    private String nom;

    @Field(value="prenom")
    private String prenom;

    @Field(value="date")
    private String date;

    @Field(value="mel")
    private String mel;

    public TestCV(String nom, String prenom, String date, String mel) {
        super();
        this.id = compteur++;
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
        this.mel = mel;
    }

    public TestCV() {}

    @Override
    public String toString() {
        return ("CV (" + id + ") [" + nom + " " + prenom
                + " ,Date nais=" + date + ", mel=" + mel);
    }
}
