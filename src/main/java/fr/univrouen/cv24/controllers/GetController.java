package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.util.Fichier;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.client.model.Filters.eq;

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
        return "Test : " + "<br>" + "id = " + id + "<br>titrezvhagyaezyga = " + title;
    }

    @GetMapping("/testfic")
    public String testfic() {
        return Fichier.loadFileXML();
    }

    @GetMapping("/cv24/html")
    public String getCVById(
            @RequestParam(value = "id") int id
    ) {
        MongoClient mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        Document doc = collection.find(eq("id", id)).first();
        return doc == null ? "not found" : doc.toJson();
    }

}
