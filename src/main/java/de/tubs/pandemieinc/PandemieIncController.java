package de.tubs.pandemieinc;

import de.tubs.pandemieinc.implementations.BogoImplementation;
import de.tubs.pandemieinc.implementations.EndRoundImplementation;
import de.tubs.pandemieinc.implementations.FileLoggerDecorator;
import de.tubs.pandemieinc.implementations.MedDeadlyFirstImplementation;
import de.tubs.pandemieinc.implementations.MedFastFirstImplementation;
import de.tubs.pandemieinc.implementations.MedSlowFirstImplementation;
import de.tubs.pandemieinc.implementations.PandemieImpl;
import de.tubs.pandemieinc.implementations.VaccDeadlyFirstImplementation;
import de.tubs.pandemieinc.implementations.VaccFastFirstImplementation;
import de.tubs.pandemieinc.implementations.VaccSlowFirstImplementation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
* Controller class to map our implementations to specific URL endpoints.
*/
@RestController
public class PandemieIncController {

    /**
    * BogoImplementation, a simple implementation that
    * randomly decides a "possible" action.
    *
    * Available on /bogo (e.g. localhost:8080/bogo)
    */
    @RequestMapping(
            value = {"/bogo", "/bogo/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String bogoImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        // Use "BogoSort" to select an action
        PandemieImpl bogo;
        if (id != null) {
            bogo = new FileLoggerDecorator(BogoImplementation::new);
            ((FileLoggerDecorator) bogo).path = "/tmp/pandemieinc/" + id;
        } else {
            bogo = new BogoImplementation();
        }
        String action = bogo.selectAction(round);
        return action;
    }

    /**
    * VaccDeadlyFirstImplementation, an implementation
    * that tries to defeat the deadly pathogen at beginning and
    * then develops vaccines for the other pathogens.
    *
    * Available on /deadlyVacc (e.g. localhost:8080/deadlyVacc)
    */
    @RequestMapping(
            value = {"/deadlyVacc", "/deadlyVacc/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String deadlyVaccImplmentation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(VaccDeadlyFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new VaccDeadlyFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * MedDeadlyFirstImplementation, an implementation
    * that tries to defeat the deadly pathogen at beginning and
    * then develops medications for the other pathogens.
    *
    * Available on /deadlyMed (e.g. localhost:8080/deadlyMed)
    */
    @RequestMapping(
            value = {"/deadlyMed", "/deadlyMed/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String deadlyMedImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(MedDeadlyFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new MedDeadlyFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * VaccFastFirstImplementation, an implementation
    * that tries to defeat the fastest pathogen at beginning and
    * then develops vaccines for the other pathogens.
    *
    * Available on /fastVacc (e.g. localhost:8080/fastVacc)
    */
    @RequestMapping(
            value = {"/fastVacc", "/fastVacc/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String fastVaccImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(VaccFastFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new VaccFastFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * MedFastFirstImplementation, an implementation
    * that tries to defeat the fastest pathogen at beginning and
    * then develops medications for the other pathogens.
    *
    * Available on /fastMed (e.g. localhost:8080/fastMed)
    */
    @RequestMapping(
            value = {"/fastMed", "/fastMed/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String fastMedImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(MedFastFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new MedFastFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * VaccSlowFirstImplementation, an implementation
    * that tries to defeat the slowest pathogen at beginning and
    * then develops vaccines for the other pathogens.
    *
    * Available on /slowVacc (e.g. localhost:8080/slowVacc)
    */
    @RequestMapping(
            value = {"/slowVacc", "/slowVacc/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String slowVaccImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(VaccSlowFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new VaccSlowFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * MedSlowFirstImplementation, an implementation
    * that tries to defeat the slowest pathogen at beginning and
    * then develops medications for the other pathogens.
    *
    * Available on /slowMed (e.g. localhost:8080/slowMed)
    */
    @RequestMapping(
            value = {"/slowMed", "/slowMed/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String slowMedImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(MedSlowFirstImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new MedSlowFirstImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * EndRoundImplementation, an implementation
    * that only returns "endRound" for learning and documentation
    * purposes.
    *
    * Available on /endRound (e.g. localhost:8080/endRound)
    */
    @RequestMapping(
            value = {"/endRound", "/endRound/{id}"},
            method = RequestMethod.POST,
            produces = "application/json")
    public String endRoundImplementation(
            @PathVariable(required = false) String id, @RequestBody Round round) {
        PandemieImpl impl;
        if (id != null) {
            impl = new FileLoggerDecorator(EndRoundImplementation::new);
            ((FileLoggerDecorator) impl).path = "/tmp/pandemieinc/" + id;
        } else {
            impl = new EndRoundImplementation();
        }
        String action = impl.selectAction(round);
        return action;
    }

    /**
    * Index page for accidental access through the web. ;-)
    */
    @RequestMapping("/")
    public String index() {
        return "<html><head><title>Nothing to see here ;-)</title></head><body><h1>Hoi!</h1></body></html>";
    }
}
