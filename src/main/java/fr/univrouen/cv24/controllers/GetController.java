package fr.univrouen.cv24.controllers;

import fr.univrouen.cv24.model.*;
import fr.univrouen.cv24.repository.CV24Repository;
import fr.univrouen.cv24.repository.TestCVRepository;
import fr.univrouen.cv24.util.Fichier;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class GetController {

    @Autowired
    private TestCVRepository testCVRepository;

    @Autowired
    private CV24Repository cv24Repository;

    @GetMapping("/resume")
    public String getListCVinXML() {
        return "Envoi de la liste des CV";
    }

    @GetMapping("/cvid")
    public String getCVinXML(
        @RequestParam(value = "texte") String texte) {
        return "Détail du CV n°" + texte;
    }

    @GetMapping("/test")
    public String doTest(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "titre") String title) {
        return "Test : " + "<br>" + "id = " + id + "<br>titrezvhagyaezyga = " + title;
    }

    @GetMapping("/testfic")
    public String testfic() {
        return Fichier.loadFileXML();
    }


    @GetMapping("/testInsert")
    public String testInsert() {

        CV24 maxCV = cv24Repository.findTopByOrderByIdDesc();
        Integer id = maxCV == null ? 1 : maxCV.getId() + 1;

        // Identite
        String name = "Albert";
        String firstName = "Gilbert";
        String tel = "02 35 61 89 03";
        String mel = null;
        String genre = "M.";
        Identite identite = new Identite(genre, name, firstName, tel, mel);

        // Objectif
        String intitule = "ch$omeur";
        String statut = "alternance";
        Objectif objectif = new Objectif(intitule, statut);

        // Prof

        Date datedeb = new Date();
        Date datefin = new Date();
        String titre = "profession intéressante";

        Detail detail = new Detail(datedeb, datefin, titre);
        Detail detail2 = new Detail(datedeb, datefin, titre);

        List<Detail> detailList = new ArrayList<>();
        detailList.add(detail);
        detailList.add(detail2);
        Prof prof = new Prof(detailList);

        // Competences
        int niveau = 4865211;
        Date date = new Date();
        String institut = null;
        List<String> titreList = new ArrayList<>();
        titreList.add("ehuauheahu");
        Diplome diplome = new Diplome(niveau, date, institut, titreList);

        Certif certif = new Certif(datedeb, datefin, titre);
        List<Certif> certifList = new ArrayList<>();
        certifList.add(certif);
        Competences competences = new Competences(diplome, certifList);

        // Divers

        String lang = "fr";
        String cert = "MAT";
        String nivs = "A1";
        Integer nivi = 12;
        Langue langue1 = new Langue(lang, cert, nivs, null);
        Langue langue2 = new Langue(lang, cert, null, nivi);

        List<Langue> lv = new ArrayList<>();
        lv.add(langue1);
        lv.add(langue2);

        String comment = "je suis un commentaire très utile";

        Autre autre = new Autre(titre, comment);

        List<Autre> autreList = new ArrayList<>();
        autreList.add(autre);

        Divers divers = new Divers(lv, autreList);

        CV24 CV = new CV24(id, identite, objectif, prof, competences, divers);

        this.cv24Repository.insert(CV);

        return "GOOD" + CV;
    }

}
