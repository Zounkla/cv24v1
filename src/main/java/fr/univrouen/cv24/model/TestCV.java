package fr.univrouen.cv24.model;

import fr.univrouen.cv24.repository.TestCVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Document(collection = "CV")
public class TestCV implements Serializable {
    private static final long serialVersionUID = 2024L;

    @Id
    private Integer id;

    private String nom;
    private String prenom;
    private String date;
    private String mel;

    public TestCV(Integer id, String nom, String prenom, String date, String mel) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
        this.mel = mel;
    }

    public TestCV() {}

    public Integer getId() {
        return id;
    }
    @Override
    public String toString() {
        return ("CV (" + id + ") [" + nom + " " + prenom
                + " ,Date nais=" + date + ", mel=" + mel);
    }
}
