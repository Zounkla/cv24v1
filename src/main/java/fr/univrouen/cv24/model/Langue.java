package fr.univrouen.cv24.model;

import io.swagger.v3.oas.models.security.SecurityScheme;

public class Langue {

    private String lang;
    private String cert;
    private String nivs;
    private Integer nivi;

    public Langue(String lang, String cert, String nivs, Integer nivi) {
        this.lang = lang;
        this.cert = cert;
        this.nivs = nivs;
        this.nivi = nivi;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getNivs() {
        return nivs;
    }

    public void setNivs(String nivs) {
        this.nivs = nivs;
    }

    public Integer getNivi() {
        return nivi;
    }

    public void setNivi(Integer nivi) {
        this.nivi = nivi;
    }
}
