package fr.univrouen.cv24.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class Competences {

    private List<Diplome> diplome;

    private List<Certif> certifList;

    public List<Diplome> getDiplome() {
        return diplome;
    }

    public void setDiplome(List<Diplome> diplome) {
        this.diplome = diplome;
    }

    public List<Certif> getCertifList() {
        return certifList;
    }

    public void setCertifList(List<Certif> certifList) {
        this.certifList = certifList;
    }
}
