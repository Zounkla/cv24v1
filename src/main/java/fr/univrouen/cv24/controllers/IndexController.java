package fr.univrouen.cv24.controllers;

import fr.univrouen.cv24.model.TestCV;
import fr.univrouen.cv24.repository.TestCVRepository;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        Resource resource = new DefaultResourceLoader().getResource("img/logo.png");
        String url = resource.getFilename();
        String result = """
                <!DOCTYPE HTML>
                <html lang='fr'>
                    <head> 
                        <title>CV24</title>
                        <meta charset='UTF-8'> 
                    </head>
                    
                    <body>
                        <h1>NOM DU PROJET</h1>
                        <h2> V1.0 </h2>
                        
                        <h2> <u> MONTAGNE Erwann </u> </h2>
                        <h2> <u> VAN LIEDEKERKE Florian </u> </h2>
                        <img src=' " + url + " ' alt="logo de l'Université de Rouen Normandie"/>
                    </body>
                </html>
             """;
        return result;
    }
}
