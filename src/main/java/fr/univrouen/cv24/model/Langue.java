package fr.univrouen.cv24.model;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class Langue {
    private String lang;
    private String cert;
    private String nivs;
    private Integer nivi;

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
