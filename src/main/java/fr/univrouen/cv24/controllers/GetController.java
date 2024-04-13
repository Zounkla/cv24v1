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


    @GetMapping("/testInsert")
    public String testInsert() {
        TestCV maxCV = testCVRepository.findTopByOrderByIdDesc();
        Integer id = maxCV == null ? 1 : maxCV.getId() + 1;
        String name = "Louis";
        String firstName = "Jean";
        String date = "today";
        String mel = "Jean.Louis@gmail.com";
        TestCV testCV = new TestCV(id, name, firstName, date, mel);

        this.testCVRepository.insert(testCV);

        return "Inserted: " + testCV;
    }

}
