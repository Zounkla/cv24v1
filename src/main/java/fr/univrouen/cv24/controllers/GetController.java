package fr.univrouen.cv24.controllers;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import fr.univrouen.cv24.model.*;
import fr.univrouen.cv24.repository.CV24Repository;
import fr.univrouen.cv24.util.Fichier;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.util.BsonUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
@RestController
public class GetController {

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

    @GetMapping("/cv24/html")
    public String getCVById(
            @RequestParam(value = "id") int id
    ) {
        /*Optional<CV24> cv = cv24Repository.findById(id);
        if (cv.isPresent()) {
            CV24 cv24 = cv.get();
            Bson bson = BsonUtils.asBson(cv24);
            Document doc = BsonUtils.asDocument(bson);
            return BsonUtils.toJson(doc);
        }
        return "";*/
        MongoClient mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        return  collection.find().first().toJson();
    }

    @GetMapping("/testInsert")
    public String testInsert() {

        /*CV24 maxCV = cv24Repository.findTopByOrderByIdDesc();
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

        Divers divers = getDivers(titre);

        CV24 CV = new CV24(id, identite, objectif, prof, competences, divers);

        this.cv24Repository.insert(CV);

        return "GOOD" + CV;
    }*/

        String fileName = "xml/cv24.tp2a.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String xmlFilePath = file.getAbsolutePath();
        Path xmlDoc = Paths.get(xmlFilePath);
        String XML_STRING = null;
        try {
            XML_STRING = Files.lines(xmlDoc).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject xmlJsonObject = XML.toJSONObject(XML_STRING);
        String jsonString = xmlJsonObject.toString();
        Document doc = Document.parse(jsonString);
        MongoClient mongo = null;
        mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        InsertOneResult result = collection.insertOne(doc);
        return result.toString();
    }
    private static Divers getDivers(String titre) {
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
        return divers;
    }

}
