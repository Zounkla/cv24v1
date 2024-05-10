package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class DeleteController {

    @DeleteMapping(value = "/cv24/delete/{id}")
    public String delete(@PathVariable Integer id) {
        MongoClient mongo;
        mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        Document cv = collection.find(eq("id", id)).first();
        if(cv == null) {
            return errorDelete("NOT IN DATABASE");
        }
        collection.deleteOne(cv);
        return successDelete(id);
    }

    /**
     * errorInsert : returns an XML formatted String showing the error while deleting the CV
     * @param reason the reason the error occurred
     * @return String
     */
    private String errorDelete(String reason) {
        return """
                <error>
                    <status>ERROR</status>
                    <detail>%s</detail>
                </error>
                """.formatted(reason);
    }

    /**
     * successDelete : returns an XML formatted String showing the success while deleting the CV
     * @param id the id of the CV deleted
     * @return String
     */
    private String successDelete(int id) {
        return """
                <success>
                    <id>%d</id>
                    <status>DELETED</status>
                </success>
                """.formatted(id);
    }

}
