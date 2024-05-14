package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private CVService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully inserted"),
            @ApiResponse(responseCode = "403", description = "CV not matching XSD or already inserted")
    })
    @Operation(summary = "Insert a CV", description = "Inserts a new CV into Database")
    @RequestMapping(value="/cv24/insert", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> insert(@RequestBody String cv) {
        if (!service.isValidXML(cv)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    service.errorInsert("INVALID")
            );
        }
        JSONObject xmlJsonObject = XML.toJSONObject(cv);
        String jsonString = xmlJsonObject.toString();
        Document doc = Document.parse(jsonString);
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            if (service.identityAlreadyInDB(cv, collection)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        service.errorInsert("DUPLICATED")
                );
            }
            int id = service.getNextId(collection);
            doc.append("id", id);
            collection.insertOne(doc);
            return ResponseEntity.ok(service.successInsert(id));
        }
    }
}
