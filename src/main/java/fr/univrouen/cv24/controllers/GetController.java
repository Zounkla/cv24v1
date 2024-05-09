package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
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

    @GetMapping("/cv24/xml")
    public String getCVById(
            @RequestParam(value = "id") int id
    ) {
        MongoClient mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        Document doc = collection.find(eq("id", id)).first();
        if (doc == null) {
            return "not found";
        }
        return documentToXML(collection, id);
    }

    private String documentToXML(MongoCollection<Document> collection, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "<xmp>" + "\n");
        stringBuilder.append("<cv24:cv24 xmlns:cv24=\"http://univ.fr/cv24\">" + "\n");
        stringBuilder.append(printIdentity(collection, id));
        stringBuilder.append("</cv:24>" + "\n" + "</xmp>");
        return stringBuilder.toString();
    }

    private String printIdentity(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                "cv24:cv24.cv24:identite.cv24:nom"),
                Projections.excludeId());
        String name = collection.find(filter).projection(projection).first().toString();
        projection = Projections.fields(Projections.include(
                "cv24:cv24.cv24:identite.cv24:genre"),
                Projections.excludeId());
        String gender = collection.find(filter).projection(projection).first().toString();
        projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:identite.cv24:prenom"),
                Projections.excludeId());
        String firstName = collection.find(filter).projection(projection).first().toString();
        projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:identite.cv24:tel"),
                Projections.excludeId());
        Document tel = collection.find(filter).projection(projection).first();
        projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:identite.cv24:mel"),
                Projections.excludeId());
        Document mel = collection.find(filter).projection(projection).first();
        String identityBeginning =
                """
                    <cv24:identite>
                        <cv24:genre>%s</cv24:genre>
                        <cv24:nom>%s</cv24:nom>
                        <cv24:prenom>%s</cv24:prenom>
                """.formatted(getInfoFromDB(gender, "cv24:genre="),
                        getInfoFromDB(name, "cv24:nom="),
                        getInfoFromDB(firstName, "cv24:prenom="));
        if (tel != null && !tel.isEmpty()) {
            identityBeginning +=
                    """
                            <cv24:tel>%s</cv24:tel>
                    """.formatted(getInfoFromDB(tel.toString(), "cv24:tel="));
        }
        if (mel != null && !mel.isEmpty()) {
            identityBeginning +=
                    """
                            <cv24:mel>%s</cv24:mel>
                    """.formatted(getInfoFromDB(mel.toString(), "cv24:mel="));
        }
        String identityEnding =
        """
            </cv24:identite>
        """;
        return identityBeginning + identityEnding;
    }

    private String getInfoFromDB(String field, String result) {
        int beginIndex = field.indexOf(result) + result.length();
        int endIndex = field.indexOf("}");
        return field.substring(beginIndex, endIndex);
    }

}
