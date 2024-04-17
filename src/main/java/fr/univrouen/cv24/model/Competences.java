package fr.univrouen.cv24.model;

import java.util.List;

public class Competences {

    private Diplome diplome;

    private List<Certif> certifList;

    public Competences(Diplome diplome, List<Certif> certifList) {
        this.diplome = diplome;
        this.certifList = certifList;
    }

    public Diplome getDiplome() {
        return diplome;
    }

    public void setDiplome(Diplome diplome) {
        this.diplome = diplome;
    }

    public List<Certif> getCertifList() {
        return certifList;
    }

    public void setCertifList(List<Certif> certifList) {
        this.certifList = certifList;
    }
}
