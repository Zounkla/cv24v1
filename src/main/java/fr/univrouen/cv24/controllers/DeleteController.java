package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.CVService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class DeleteController {

    @Autowired
    private CVService service;

    @DeleteMapping(value = "/cv24/delete")
    public String delete(@RequestParam(value = "id") Integer id) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document cv = collection.find(eq("id", id)).first();
            if (cv == null) {
                return service.errorDelete("NOT IN DATABASE");
            }
            collection.deleteOne(cv);
            return service.successDelete(id);
        }
    }
}
