package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.univrouen.cv24.CVService;
import fr.univrouen.cv24.model.Autre;
import fr.univrouen.cv24.model.CV24;
import fr.univrouen.cv24.model.Langue;
import fr.univrouen.cv24.repository.CV24Repository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class GetController {

    @Autowired
    private CVService service;

    @GetMapping("/cv24/xml")
    public String getCVById(
            @RequestParam(value = "id") int id
    ) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document doc = collection.find(eq("id", id)).first();
            if (doc == null) {
                return service.printErrorNotFoundXML(id);
            }
            return service.documentToXML(collection, id);
        }
    }

    @GetMapping("/cv24/html")
    public String getCVByIdHTML(
            @RequestParam(value = "id") int id
    ) {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            Document doc = collection.find(eq("id", id)).first();
            if (doc == null) {
                return service.printErrorNotFoundHTML(id);
            }
            String XMLFile = service.documentToXML(collection, id);
            Source xslt = new StreamSource("src/main/resources/xml/CV.xslt");
            Source xml = new StreamSource(new StringReader(XMLFile));
            return service.transformXMLToHTML(xml, xslt);
        }
    }

    @GetMapping("/cv24/resume/xml")
    public String resumeXML() {
        try (MongoClient mongo = MongoClients.create(service.getMongoURI())) {
            //Connecting to the database
            MongoDatabase database = mongo.getDatabase("main");
            MongoCollection<Document> collection = database.getCollection("CV24");
            return service.resumeXML(collection);
        }
    }

    @GetMapping("/cv24/resume")
    public String resumeHTML() {
        String XML = resumeXML();
        Source xslt = new StreamSource("src/main/resources/xml/CVResume.xslt");
        Source xml = new StreamSource(new StringReader(XML));
        return service.transformXMLToHTML(xml, xslt);
    }
}
