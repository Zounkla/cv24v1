package fr.univrouen.cv24.model;

import java.util.List;

public class Prof {

    private List<Detail> details;

    public Prof(List<Detail> details) {
        this.details = details;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
