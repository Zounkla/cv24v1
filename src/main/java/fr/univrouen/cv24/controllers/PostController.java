package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class PostController {

    @RequestMapping(value = "/testpost", method = RequestMethod.POST, consumes = "application/xml")
    public String postTest(@RequestBody String flux) {
        return ("<result><response>Message reçu : </response>"
            + flux + "</result>");
    }

    @RequestMapping(value="/insert", method = RequestMethod.POST, consumes = "application/xml")
    public String insert(@RequestBody String cv) {

        JSONObject xmlJsonObject = XML.toJSONObject(cv);
        String jsonString = xmlJsonObject.toString();
        Document doc = Document.parse(jsonString);
        MongoClient mongo;
        mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        int id = 0;
        Document oldDoc = collection.find(eq("id", id)).first();
        while (oldDoc != null) {
            ++id;
            oldDoc = collection.find(eq("id", id)).first();
        }
        doc.append("id", id);
        InsertOneResult result = collection.insertOne(doc);
        return result.toString();
    }

}
