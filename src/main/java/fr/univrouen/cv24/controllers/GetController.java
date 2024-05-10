package fr.univrouen.cv24.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class GetController {

    @GetMapping("/resume")
    public String getListCVinXML() {
        return "Envoi de la liste des CV";
    }

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
            return "not found";
        }
        return documentToXML(collection, id);
    }

    private String documentToXML(MongoCollection<Document> collection, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "<xmp>" + "\n");
        stringBuilder.append("<cv24:cv24 xmlns:cv24=\"http://univ.fr/cv24\">" + "\n");
        stringBuilder.append(printIdentity(collection, id));
        stringBuilder.append(printObjective(collection, id));
        stringBuilder.append(printProf(collection, id));
        stringBuilder.append(printCompetences(collection, id));
        stringBuilder.append("</cv24:cv24>" + "\n" + "</xmp>");
        return stringBuilder.toString();
    }

    private String printIdentity(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
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
        String identityBeginning =
                """
                    <cv24:identite>
                        <cv24:genre>%s</cv24:genre>
                        <cv24:nom>%s</cv24:nom>
                        <cv24:prenom>%s</cv24:prenom>
                """.formatted(getInfoFromDB(gender, "cv24:genre="),
                        getInfoFromDB(name, "cv24:nom="),
                        getInfoFromDB(firstName, "cv24:prenom="));
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
        String identityEnding =
        """
            </cv24:identite>
        """;
        return identityBeginning + identityEnding;
    }

    private String printObjective(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:objectif"),
                Projections.excludeId());
        String objective = collection.find(filter).projection(projection).first().toString();
        return """
                    <cv24:objectif statut="%s">%s</cv24:objectif>
                """.formatted(
                        getInfoFromDB(objective, "statut=", ","),
                               getInfoFromDB(objective, "content="));
    }

    private String printProf(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
        Bson projection = Projections.fields(Projections.include(
                        "cv24:cv24.cv24:prof"),
                Projections.excludeId());
        String prof = collection.find(filter).projection(projection).first().toString();
        if (!prof.contains("cv24:prof")) {
            return "";
        }
        return """
                    <cv24:prof>
                        %s
                    </cv24:prof>
                """.formatted(printDetails(collection, id));
    }

    private String printDetails(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
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
               <cv24:detail>
                    <cv24:datedeb>%s</cv24:datedeb>
                """.formatted(beginDate);
        if (!endDate.isEmpty()) {
            result +=
                    """
                        <cv24:datefin>%s</cv24:datefin>
                    """.formatted(endDate);
        }
        result += """
                    <cv24:titre>%s</cv24:titre>
               </cv24:detail>
                """.formatted(title);
        return result;
    }

    private String printCompetences(MongoCollection<Document> collection, int id) {
        return
                """
                    <cv24:competences>
                        %s
                        %s
                    </cv24:competences>
                """.formatted(printDiplomas(collection, id), printCertifs(collection, id));
    }

    private String printDiplomas(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
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
            result.append(printDiploma(diplomas));
        }
        return result.toString();
    }

    private String printDiploma(String diplomas) {
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
        String result = """
               <cv24:diplome="%s">
                    <cv24:date>%s</cv24:date>
                """.formatted(level, date);
        if (!institute.isEmpty()) {
            result +=
                    """
                        <cv24:institut>%s</cv24:institut>
                    """.formatted(institute);
        }
        result += """
                    <cv24:titre>%s</cv24:titre>
               </cv24:diplome>
                """.formatted(title);
        return result;
    }

    private String printCertifs(MongoCollection<Document> collection, int id) {
        Bson filter = Filters.eq("id", id);
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
               <cv24:certif>
                    <cv24:datedeb>%s</cv24:datedeb>
                """.formatted(dateBegin);
        if (!dateEnd.isEmpty()) {
            result +=
                    """
                        <cv24:datefin>%s</cv24:datefin>
                    """.formatted(dateEnd);
        }
        result += """
                    <cv24:titre>%s</cv24:titre>
               </cv24:certif>
                """.formatted(title);
        return result;
    }

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

    private String getInfoFromDB(String field, String result) {
        return getInfoFromDB(field, result, "}");
    }

}
