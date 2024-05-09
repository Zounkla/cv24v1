package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;

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
        if (!isValidXML(cv)) {
            return errorInsert("INVALID");
        }
        JSONObject xmlJsonObject = XML.toJSONObject(cv);
        String jsonString = xmlJsonObject.toString();
        Document doc = Document.parse(jsonString);
        MongoClient mongo;
        mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        if (identityAlreadyInDB(cv, collection)) {
            return errorInsert("DUPLICATED");
        }
        int id = 0;
        Document oldDoc = collection.find(eq("id", id)).first();
        while (oldDoc != null) {
            ++id;
            oldDoc = collection.find(eq("id", id)).first();
        }
        doc.append("id", id);
        collection.insertOne(doc);
        return successInsert(id);
    }


    /**
     * identityAlreadyInDB : checks if a CV with gender, name, firstname and phone number
     *          is already in database
     * @param cv the new CV converted to String
     * @param collection the collection in DB representing the CVs
     * @return boolean
     */
    private boolean identityAlreadyInDB(String cv, MongoCollection<Document> collection) {
        String cvName = getInfoFromCV(cv, "<cv24:nom>");
        String cvFirstName = getInfoFromCV(cv, "<cv24:prenom>");
        String cvGender = getInfoFromCV(cv, "<cv24:genre>");
        String cvTel = getInfoFromCV(cv, "<cv24:tel>");
        Bson nameFilter = Filters.eq("cv24:cv24.cv24:identite.cv24:nom", cvName);
        Bson firstNameFilter = Filters.eq("cv24:cv24.cv24:identite.cv24:prenom", cvFirstName);
        Bson genderFilter = Filters.eq("cv24:cv24.cv24:identite.cv24:genre", cvGender);
        Bson telFilter = Filters.eq("cv24:cv24.cv24:identite.cv24:tel", cvTel);
        Document collectionCV;
        if (cvTel == null) {
            collectionCV = collection.find(Filters.and(nameFilter, firstNameFilter,
                    genderFilter)).first();
        } else {
            collectionCV = collection.find(Filters.and(nameFilter, firstNameFilter,
                    genderFilter, telFilter)).first();
        }
        return collectionCV != null;
    }

    /**
     * getInfoFromCV : returns the new information given by the user in the new CV
     * @param cv the new CV converted as a String
     * @param info the searched information as an XML tag (for example, <info>)
     * @return String
     */
    private String getInfoFromCV(String cv, String info) {
        int beginIndex = cv.indexOf(info);
        int length = info.length();
        String closingTag = new StringBuilder(info).insert(1, "/").toString();
        int endIndex = cv.indexOf(closingTag);
        if (endIndex == -1) {
            return null;
        }
        return cv.substring(beginIndex + length, endIndex);
    }

    /**
     * errorInsert : returns an XML formatted String showing the error while inserting the CV
     * @param reason the reason the error occurred
     * @return String
     */
    private String errorInsert(String reason) {
        return """
                <error>
                    <status>ERROR</status>
                    <detail>%s</detail>
                </error>
                """.formatted(reason);
    }

    /**
     * successInsert : returns an XML formatted String showing the success while inserting the CV
     * @param id the id of the CV inserted
     * @return String
     */
    private String successInsert(int id) {
        return """
                <success>
                    <id>%d</id>
                    <status>INSERTED</status>
                </success>
                """.formatted(id);
    }

    /**
     * checks if a CV is valid against CV XSD
     * @param CV the parsed CV
     * @return boolean
     */
    private boolean isValidXML(String CV) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        try {
            spf.setSchema(sf.newSchema(
                    new Source[] {new StreamSource("src/main/resources/xml/cv24.tp1.xsd")}
            ));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        SAXParser parser;
        try {
            parser = spf.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        XMLReader reader;
        try {
            reader = parser.getXMLReader();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        SimpleErrorHandler seh = new SimpleErrorHandler();
        reader.setErrorHandler(seh);
        try {
            try {
                reader.parse(new InputSource(new StringReader(CV)));
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return !seh.hasError();
    }

    private static class SimpleErrorHandler implements ErrorHandler {

        private boolean errorOccured;

        public SimpleErrorHandler() {

        }

        public boolean hasError() {
            return errorOccured;
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            errorOccured = true;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errorOccured = true;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            errorOccured = true;
        }
    }
}
