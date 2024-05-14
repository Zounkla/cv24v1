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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class GetController {

    @Autowired
    private CVService service;

    @GetMapping(value = "/cv24/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The CV was not found")
    })
    @Operation(summary = "Get a CV by id (XML formatted)",
            description = "Returns an XML CV as per the id")
    public ResponseEntity<String> getCVById(
            @Parameter(name = "id", description = "CV id", example = "0")
            @RequestParam(value = "id") int id
    ) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document doc = collection.find(eq("id", id)).first();
            if (doc == null) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        service.printErrorNotFoundXML(id)
                );
            }
            return ResponseEntity.ok(service.documentToXML(collection, id));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The CV was not found")
    })
    @Operation(summary = "Get a CV by id (HTML formatted)",
            description = "Returns an HTML CV as per the id")
    @GetMapping(value = "/cv24/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getCVByIdHTML(
            @Parameter(name = "id", description = "CV id", example = "0")
            @RequestParam(value = "id") int id
    ) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document doc = collection.find(eq("id", id)).first();
            if (doc == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        service.printErrorNotFoundHTML(id)
                );
            }
            String XMLFile = service.documentToXML(collection, id);
            Source xslt = new StreamSource("src/main/resources/xml/CV.xslt");
            Source xml = new StreamSource(new StringReader(XMLFile));
            return ResponseEntity.ok(service.transformXMLToHTML(xml, xslt));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @Operation(summary = "Get all CVs (XML formatted)",
            description = "Returns an XML resume of all CVs")
    @GetMapping(value = "/cv24/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> resumeXML() {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            return ResponseEntity.ok(service.resumeXML(collection));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @Operation(summary = "Get all CVs (HTML formatted)",
            description = "Returns an HTML resume of all CVs")
    @GetMapping("/cv24/resume")
    public ResponseEntity<String> resumeHTML() {
        String XML = resumeXML().getBody();
        Source xslt = new StreamSource("src/main/resources/xml/CVResume.xslt");
        assert XML != null;
        Source xml = new StreamSource(new StringReader(XML));
        return ResponseEntity.ok(service.transformXMLToHTML(xml, xslt));
    }
}
