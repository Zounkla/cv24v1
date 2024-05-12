package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.CVService;
import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private CVService service;

    @RequestMapping(value="/cv24/insert", method = RequestMethod.POST, consumes = "application/xml")
    public String insert(@RequestBody String cv) {
        if (!service.isValidXML(cv)) {
            return service.errorInsert("INVALID");
        }
        JSONObject xmlJsonObject = XML.toJSONObject(cv);
        String jsonString = xmlJsonObject.toString();
        Document doc = Document.parse(jsonString);
        MongoClient mongo;
        mongo = MongoClients.create(CVService.MONGO_URL);
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        if (service.identityAlreadyInDB(cv, collection)) {
            return service.errorInsert("DUPLICATED");
        }
        int id = service.getNextId(collection);
        doc.append("id", id);
        collection.insertOne(doc);
        return service.successInsert(id);
    }
}
