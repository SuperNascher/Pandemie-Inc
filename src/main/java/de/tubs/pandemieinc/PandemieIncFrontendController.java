package de.tubs.pandemieinc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
* Controller class to to map Fronend/Webbrowser content.
*/
@Controller
public class PandemieIncFrontendController {

    /**
    * Index page for accidental access through the web. ;-)
    */
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    /**
    * World map for the game
    */
    @RequestMapping("/worldmap")
    public String worldmap() {
        return "worldmap.html";
    }
}
