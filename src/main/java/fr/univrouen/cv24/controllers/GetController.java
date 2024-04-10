package fr.univrouen.cv24.controllers;

import fr.univrouen.cv24.model.TestCV;
import fr.univrouen.cv24.repository.TestCVRepository;
import fr.univrouen.cv24.util.Fichier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetController {

    @Autowired
    private TestCVRepository testCVRepository;
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

    @RequestMapping(value = "/testxml", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody TestCV getXML() {
        return new TestCV("HAMILTON", "Margaret", "1969/07/21",
                "Appollo11@nasa.us");
    }

    @GetMapping("/testInsert")
    public String testInsert() {
        String name = "Louis";
        String firstName = "Jean";
        String date = "today";
        String mel = "Jean.Louis@gmail.com";
        TestCV testCV = new TestCV(name, firstName, date, mel);

        this.testCVRepository.insert(testCV);

        return "Inserted: " + testCV;
    }

}
