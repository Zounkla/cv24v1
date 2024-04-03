package fr.univrouen.cv24.controllers;

import fr.univrouen.cv24.model.TestCV;
import fr.univrouen.cv24.util.Fichier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetController {


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
        return "Test : " + "<br>" + "id = " + id + "<br>titre = " + title;
    }

    @GetMapping("/testfic")
    public String testfic() {
        return Fichier.loadFileXML();
    }

    @RequestMapping(value = "/testxml", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody TestCV getXML() {
        return new TestCV("HAMILTON", "Margaret", "1969/07/21",
                "Appollo11@nasa.us");
    }

}
