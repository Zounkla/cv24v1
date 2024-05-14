package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class DeleteController {

    @Autowired
    private CVService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    @Operation(summary = "Delete a CV", description = "Deletes a CV as per the id")
    @DeleteMapping(value = "/cv24/delete")
    public ResponseEntity<String> delete(
            @Parameter(name = "id", description = "CV id", example = "0")
            @RequestParam(value = "id") Integer id
    ) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document cv = collection.find(eq("id", id)).first();
            if (cv == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        service.errorDelete("NOT IN DATABASE")
                );
            }
            collection.deleteOne(cv);
            return ResponseEntity.ok(service.successDelete(id));
        }
    }
}
