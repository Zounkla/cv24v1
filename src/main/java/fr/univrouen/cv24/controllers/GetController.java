package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class GetController {

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
            return printErrorNotFoundXML(id);
        }
        return documentToXML(collection, id);
    }

    @GetMapping("/cv24/html")
    public String getCVByIdHTML(
            @RequestParam(value = "id") int id
    ) {
        MongoClient mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        Document doc = collection.find(eq("id", id)).first();
        if (doc == null) {
            return printErrorNotFoundHTML(id);
        }
        String XMLFile = documentToXML(collection, id);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Source xslt = new StreamSource("src/main/resources/xml/CV.xslt");
        Source xml = new StreamSource(new StringReader(XMLFile));
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer(xslt);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(xml, new StreamResult(writer));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    @GetMapping("/cv24/resume/xml")
    public String resumeXML() {
        MongoClient mongo = MongoClients.create("mongodb://user:resu@localhost:27017");
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("main");
        MongoCollection<Document> collection = database.getCollection("CV24");
        StringBuilder result = new StringBuilder("""
                <?xml version="1.0" encoding="UTF-8" ?>
                <xmp>
                <cvs>
                """);
        for (Document doc: collection.find()) {
            int id = (int) doc.get("id");
            result.append("    <cv>\n");
            result.append("    <id>").append(id).append("</id>\n");
            result.append(printIdentity(collection, id, false));
            result.append(printObjective(collection, id, false));
            result.append(printHighestDiploma(collection, id));
            result.append("    </cv>\n");
        }
        result.append("</cvs></xmp>");
        return result.toString();
    }

    @GetMapping("/cv24/resume")
    public String resumeHTML() {
        String XML = resumeXML();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Source xslt = new StreamSource("src/main/resources/xml/CVResume.xslt");
        Source xml = new StreamSource(new StringReader(XML));
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer(xslt);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(xml, new StreamResult(writer));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    /**
     * documentToXML: prints a CV in database to an XML format
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String documentToXML(MongoCollection<Document> collection, int id) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "<xmp>" +
                "<cv24:cv24 xmlns:cv24=\"http://univ.fr/cv24\">" + "\n" +
                printIdentity(collection, id, true) +
                printObjective(collection, id, true) +
                printProf(collection, id) +
                printCompetences(collection, id) +
                printDivers(collection, id) +
                "</cv24:cv24>" + "\n" + "</xmp>";
    }

    /**
     * printIdentity : prints all information about the identity of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @param full true if all information is displayed, false if only firstname, gender, name
     * @return String
     */
    private String printIdentity(MongoCollection<Document> collection, int id, boolean full) {
        Bson filter = eq("id", id);
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
        String identityEnding;
        String identityBeginning;
        if (!full) {
            identityBeginning =
                    """
                        <identite>
                            <genre>%s</genre>
                            <nom>%s</nom>
                            <prenom>%s</prenom>
                    """.formatted(getInfoFromDB(gender, "cv24:genre="),
                            getInfoFromDB(name, "cv24:nom="),
                            getInfoFromDB(firstName, "cv24:prenom="));
            identityEnding =
                    """
                        </identite>
                    """;
        } else {
            identityBeginning =
                    """
                        <cv24:identite>
                            <cv24:genre>%s</cv24:genre>
                            <cv24:nom>%s</cv24:nom>
                            <cv24:prenom>%s</cv24:prenom>
                    """.formatted(getInfoFromDB(gender, "cv24:genre="),
                            getInfoFromDB(name, "cv24:nom="),
                            getInfoFromDB(firstName, "cv24:prenom="));
            identityEnding =
                    """
                        </cv24:identite>
                    """;
        }
        if (!full) {
            return identityBeginning + identityEnding;
        }
        if (tel.toString().contains("cv24:tel=")) {
            identityBeginning +=
                    """
                            <cv24:tel>%s</cv24:tel>
                    """.formatted(getInfoFromDB(tel.toString(), "cv24:tel="));
        }
        if (mel.toString().contains("cv24:mel=")) {
            identityBeginning +=
                    """
                            <cv24:mel>%s</cv24:mel>
                    """.formatted(getInfoFromDB(mel.toString(), "cv24:mel="));
        }
        return identityBeginning + identityEnding;
    }

    /**
     * printObjective : prints all information about the objective of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @param full true if it's for all details of a CV, false if it's for resume
     * @return String
     */
    private String printObjective(MongoCollection<Document> collection, int id, boolean full) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:objectif"),
                Projections.excludeId());
        String objective = collection.find(filter).projection(projection).first().toString();
        if (!full) {
            return """
                    <objectif statut="%s">%s</objectif>
                """.formatted(
                    getInfoFromDB(objective, "statut=", ","),
                    getInfoFromDB(objective, "content="));
        }
        return """
                    <cv24:objectif statut="%s">%s</cv24:objectif>
                """.formatted(
                        getInfoFromDB(objective, "statut=", ","),
                               getInfoFromDB(objective, "content="));
    }

    /**
     * printProf : prints all information about the jobs of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printProf(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:prof"),
                Projections.excludeId());
        String prof = collection.find(filter).projection(projection).first().toString();
        if (!prof.contains("cv24:prof")) {
            return "";
        }
        return """
                    <cv24:prof>
                     %s    </cv24:prof>
                """.formatted(printDetails(collection, id));
    }

    /**
     * printDetails : prints all information about the details of the jobs of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printDetails(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:prof.cv24:detail"),
                Projections.excludeId());
        String details = collection.find(filter).projection(projection).first().toString();
        if (details.contains("[")) {
            details = details.substring(details.indexOf("detail=[") + "detail=[".length(),
                    details.indexOf("]"));
        } else {
            details = details.substring(details.indexOf("detail=")
                            + "detail=".length(),
                    details.indexOf("}}}}"));
        }
        StringBuilder result = new StringBuilder();
        while (details.contains("Document{{")) {
            details = details.substring(details.indexOf("Document{{")
                    + "Document{{".length());
            result.append(printDetail(details));
        }
        return result.toString();
    }

    /**
     * printIdentity : prints all information about the detail of a job in a CV
     * @param details the details about the jobs
     * @return String
     */
    private String printDetail(String details) {
        String title = getInfoFromDB(details, "cv24:titre=", ",");
        details = details.substring(details.indexOf(",") + 1);
        String beginDate = getInfoFromDB(details, "cv24:datedeb=", ",");
        details = details.substring(details.indexOf(",") + 1);
        String endDate;
        if (!details.startsWith(" cv24:datefin=")) {
            endDate = "";
            beginDate = beginDate.replaceAll("}", "");
        } else {
            endDate = getInfoFromDB(details, "cv24:datefin=", "}");
        }
        String result = """
              \t <cv24:detail>
                    \t\t<cv24:datedeb>%s</cv24:datedeb>
                """.formatted(beginDate);
        if (!endDate.isEmpty()) {
            result +=
                    """
                        \t\t<cv24:datefin>%s</cv24:datefin>
                    """.formatted(endDate);
        }
        result += """
                    \t\t<cv24:titre>%s</cv24:titre>
               \t </cv24:detail>
                """.formatted(title);
        return result;
    }

    /**
     * printCompetences : prints all information about the competences of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printCompetences(MongoCollection<Document> collection, int id) {
        return
                """
                    <cv24:competences>
                     %s%s    </cv24:competences>
                """.formatted(printDiplomas(collection, id), printCertifs(collection, id));
    }

    /**
     * printDiplomas : prints all information about the diplomas of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printDiplomas(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:competences.cv24:diplome"),
                Projections.excludeId());
        String diplomas = collection.find(filter).projection(projection).first().toString();
        if (diplomas.contains("[")) {
            diplomas = diplomas.substring(diplomas.indexOf("diplome=[") + "diplome=[".length(),
                    diplomas.indexOf("]"));
        } else {
            diplomas = diplomas.substring(diplomas.indexOf("diplome=")
                            + "diplome=".length(),
                             diplomas.indexOf("}}}}"));
        }
        StringBuilder result = new StringBuilder();
        while (diplomas.contains("Document{{")) {
            diplomas = diplomas.substring(diplomas.indexOf("Document{{")
                    + "Document{{".length());
            result.append(printDiploma(diplomas, true));
        }
        return result.toString();
    }

    /**
     * printIdentity : prints all information about a diploma in a CV
     * @param diplomas all the diplomas in the CV
     * @param full true if all diplomas are displayed, false if the last
     * @return String
     */
    private String printDiploma(String diplomas, boolean full) {
        String title = getInfoFromDB(diplomas, "cv24:titre=", ",");
        diplomas = diplomas.substring(diplomas.indexOf(",") + 1);
        String date = getInfoFromDB(diplomas, "cv24:date=", ",");
        diplomas = diplomas.substring(diplomas.indexOf(",") + 1);
        String institute;
        if (!diplomas.startsWith(" cv24:institut=")) {
            institute = "";
        } else {
            institute = getInfoFromDB(diplomas, "cv24:institut=", ",");
            diplomas = diplomas.substring(diplomas.indexOf(",") + 2);
        }
        String level = getInfoFromDB(diplomas, "niveau=", "}");
        String result;
        if (!full) {
            result = """
               \t<diplome niveau="%s">
                    \t\t<date>%s</date>
                """.formatted(level, date);
        } else {
            result = """
               \t<cv24:diplome niveau="%s">
                    \t\t<cv24:date>%s</cv24:date>
                """.formatted(level, date);
        }

        if (!institute.isEmpty()) {
            if (full) {
                result +=
                        """
                            \t\t<cv24:institut>%s</cv24:institut>
                        """.formatted(institute);
            } else {
                result +=
                        """
                            \t\t<institut>%s</institut>
                        """.formatted(institute);
            }

        }
        if (full) {
            result += """
                    \t\t<cv24:titre>%s</cv24:titre>
               \t</cv24:diplome>
                """.formatted(title);
        } else {
            result += """
                    \t\t<titre>%s</titre>
               \t</diplome>
                """.formatted(title);
        }

        return result;
    }

    /**
     * printHighestDiploma : prints all information about the latest diploma of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printHighestDiploma(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:competences.cv24:diplome"),
                Projections.excludeId());
        String diplomas = collection.find(filter).projection(projection).first().toString();
        if (diplomas.contains("[")) {
            diplomas = diplomas.substring(diplomas.indexOf("diplome=[") + "diplome=[".length(),
                    diplomas.indexOf("]"));
        } else {
            diplomas = diplomas.substring(diplomas.indexOf("diplome=")
                            + "diplome=".length(),
                    diplomas.indexOf("}}}}"));
        }
        String result = "";
        Date maxDate = null;
        while (diplomas.contains("Document{{")) {
            diplomas = diplomas.substring(diplomas.indexOf("Document{{")
                    + "Document{{".length());
            String tmp = printDiploma(diplomas, false);
            int beginIndex = tmp.indexOf("<date>") + "<date>".length();
            String dateFormat = "yyyy-MM-dd";
            String dateString = tmp.substring(beginIndex, beginIndex + dateFormat.length());
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            Date date;
            try {
                date = formatter.parse(dateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (maxDate == null || date.getTime() > maxDate.getTime()) {
                maxDate = date;
                result = printDiploma(diplomas, false);
            }
        }
        return result;
    }

    /**
     * printCertifs : prints all information about the certifications of a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printCertifs(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:competences.cv24:certif"),
                Projections.excludeId());
        String certifs = collection.find(filter).projection(projection).first().toString();
        if (certifs.contains("[")) {
            certifs = certifs.substring(certifs.indexOf("certif=[") + "certif=[".length(),
                    certifs.indexOf("]"));
        } else {
            certifs = certifs.substring(certifs.indexOf("certif=")
                            + "certif=".length(),
                    certifs.indexOf("}}}}"));
        }
        StringBuilder result = new StringBuilder();
        while (certifs.contains("Document{{")) {
            certifs = certifs.substring(certifs.indexOf("Document{{")
                    + "Document{{".length());
            result.append(printCertif(certifs));
        }
        return result.toString();
    }

    /**
     * printCertif : prints all information about a certification in a CV
     * @param certifs the certifications of the CV
     * @return String
     */
    private String printCertif(String certifs) {
        String title = getInfoFromDB(certifs, "cv24:titre=", ",");
        certifs = certifs.substring(certifs.indexOf(",") + 1);
        String dateBegin = getInfoFromDB(certifs, "cv24:datedeb=", ",");
        certifs = certifs.substring(certifs.indexOf(",") + 1);
        String dateEnd;
        if (!certifs.startsWith(" cv24:datefin=")) {
            dateEnd = "";
        } else {
            dateEnd = getInfoFromDB(certifs, "cv24:datefin=", ",");
        }
        String result = """
               \t<cv24:certif>
                    \t\t<cv24:datedeb>%s</cv24:datedeb>
                """.formatted(dateBegin);
        if (!dateEnd.isEmpty()) {
            result +=
                    """
                        \t\t<cv24:datefin>%s</cv24:datefin>
                    """.formatted(dateEnd);
        }
        result += """
                    \t\t<cv24:titre>%s</cv24:titre>
               \t</cv24:certif>
                """.formatted(title);
        return result;
    }

    /**
     * printDivers : prints all miscellaneous information about a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printDivers(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:divers"),
                Projections.excludeId());
        String divers = collection.find(filter).projection(projection).first().toString();
        if (!divers.contains("cv24:divers")) {
            return "";
        }
        return """
                    <cv24:divers>
                        %s
                        %s
                    </cv24:divers>
                """.formatted(printLVs(collection, id), printOthers(collection, id));
    }

    /**
     * printLVs : prints all languages information about a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printLVs(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:divers.cv24:lv"),
                Projections.excludeId());
        String lvs = collection.find(filter).projection(projection).first().toString();
        if (lvs.contains("[")) {
            lvs = lvs.substring(lvs.indexOf("lv=[") + "lv=[".length(),
                    lvs.indexOf("]"));
        } else {
            lvs = lvs.substring(lvs.indexOf("lv=")
                            + "lv=".length(),
                    lvs.indexOf("}}}}"));
        }
        StringBuilder result = new StringBuilder();
        while (lvs.contains("Document{{")) {
            lvs = lvs.substring(lvs.indexOf("Document{{")
                    + "Document{{".length());
            result.append(printLV(lvs));
        }
        return result.toString();
    }

    /**
     * printLV : prints all information about a language in a CV
     * @param lvs all languages in the CV
     * @return String
     */
    private String printLV(String lvs) {
        String cert = getInfoFromDB(lvs, "cert=", ",");
        lvs = lvs.substring(lvs.indexOf(",") + 1);
        String nivs = getInfoFromDB(lvs, "nivs=", ",");
        lvs = lvs.substring(lvs.indexOf(",") + 1);
        String lang;
        if (!lvs.startsWith(" lang=")) {
            lang = "";
        } else {
            lang = getInfoFromDB(lvs, "lang=", ",");
            lvs = lvs.substring(lvs.indexOf(",") + 1);
        }
        String nivi;
        if (!lvs.startsWith(" nivi=")) {
            nivi = "";
        } else {
            nivi = getInfoFromDB(lvs, "nivi=", ",");
        }
        String result = "<cv24:lv lang=\"" + lang + "\" " + "cert=\"" + cert + "\" ";
        if (!nivs.isEmpty()) {
            result += "nivs=\"" + nivs + "\" ";
        }
        if (!nivi.isEmpty()) {
            result += "nivi=\"" + nivi + "\"";
        }
        result += "/>";
        return result;
    }

    /**
     * printOthers : prints all extra information about a CV
     * @param collection the CVs in DB
     * @param id the id of the CV
     * @return String
     */
    private String printOthers(MongoCollection<Document> collection, int id) {
        Bson filter = eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:divers.cv24:autre"),
                Projections.excludeId());
        String others = collection.find(filter).projection(projection).first().toString();
        if (others.contains("[")) {
            others = others.substring(others.indexOf("autre=[") + "autre=[".length(),
                    others.indexOf("]"));
        } else {
            others = others.substring(others.indexOf("autre=")
                            + "autre=".length(),
                    others.indexOf("}}}}"));
        }
        StringBuilder result = new StringBuilder();
        while (others.contains("Document{{")) {
            others = others.substring(others.indexOf("Document{{")
                    + "Document{{".length());
            result.append(printOther(others));
        }
        return result.toString();
    }

    /**
     * printOther : prints details about extra information about a CV
     * @param others the extra information of the CV
     * @return String
     */
    private String printOther(String others) {
        String title = getInfoFromDB(others, "titre=", ",");
        others = others.substring(others.indexOf(",") + 1);
        String comment;
        if (!others.startsWith(" comment=")) {
            comment = "";
        } else {
            comment = getInfoFromDB(others, "comment=", ",");
        }
        String result = "<cv24:autre titre=\"" + title + "\" ";
        if (!comment.isEmpty()) {
            result += "comment=\"" + comment + "\"";
        }
        result += "/>";
        return result;
    }

    /**
     * getInfoFromDB: returns the information from a CV in Database
     * @param field the field coming from the Database
     * @param result the attribute searched
     * @param separator the separator
     * @return String
     */
    private String getInfoFromDB(String field, String result, String separator) {
        int beginIndex = field.indexOf(result) + result.length();
        int endIndex;
        if (!field.contains(separator)) {
            endIndex = field.length();
        } else {
            endIndex = field.indexOf(separator);
        }
        return field.substring(beginIndex, endIndex);
    }

    /**
     * getInfoFromDB: returns the information from a CV in Database
     * @param field the field coming from the Database
     * @param result the attribute searched
     * @return String
     */
    private String getInfoFromDB(String field, String result) {
        return getInfoFromDB(field, result, "}");
    }

    /**
     * printErrorNotFoundXML: prints a message error, XML formatted, about a CV not found in DB
     * @param id the id of the CV
     * @return String
     */
    private String printErrorNotFoundXML(int id) {
        return
                "<xmp>" +
                """
                <error>
                    <id>%s</id>
                    <status>ERROR</status>
                </error>
                """.formatted(id) + "</xmp>";
    }

    /**
     * printErrorNotFoundHTML: prints a message error, HTML formatted, about a CV not found in DB
     * @param id the id of the CV
     * @return String
     */
    private String printErrorNotFoundHTML(int id) {
        return
                "<p>" +
                        """
                
                            %s
                            </p>
                            <p>
                            ERROR
                        </p>
                        """.formatted(id);
    }
}
