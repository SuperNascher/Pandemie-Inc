package de.tubs.pandemieinc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tubs.pandemieinc.actions.BaseAction;
import de.tubs.pandemieinc.actions.EndRound;

@RestController
public class PandemieIncController {
    @RequestMapping(value="/dummy", method=RequestMethod.POST)
    public BaseAction dummy() {
        return new EndRound();
    }

    @RequestMapping("/")
    public String index() {
        return "<html><head><title>Nothing to see here ;-)</title></head><body><h1>Hoi!</h1></body></html>";
    }
}
