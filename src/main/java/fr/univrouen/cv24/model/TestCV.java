package fr.univrouen.cv24.model;

import jakarta.xml.bind.annotation.*;

import java.io.Serializable;

@XmlRootElement(name = "TestCV")
@XmlAccessorType(XmlAccessType.NONE)
public class TestCV implements Serializable {
    private static final long serialVersionUID = 2024L;
    private static int compteur = 1;

    @XmlAttribute
    private Integer id;

    @XmlElement
    private String nom;

    @XmlElement
    private String prenom;
    @XmlElement
    private String date;

    @XmlElement
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
