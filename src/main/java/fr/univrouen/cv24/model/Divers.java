package fr.univrouen.cv24.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Divers {

    @Field("cv24:lv")
    private Langue[] lv;
    @Field("cv24:autre")
    private Autre[] autre;

    public Divers() {
        lv = new Langue[]{};
        autre = new Autre[]{};
    }

    public Langue[] getLv() {
        return lv;
    }

    public void setLv(Langue[] lv) {
        this.lv = lv;
    }

    public Autre[] getAutre() {
        return autre;
    }

    public void setAutre(Autre[] autre) {
        this.autre = autre;
    }
}
