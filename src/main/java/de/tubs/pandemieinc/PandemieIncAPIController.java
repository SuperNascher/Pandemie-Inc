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

/** Controller class to map our implementations to specific URL endpoints. */
@RestController
@RequestMapping("/api")
public class PandemieIncAPIController {

    /**
     * BogoImplementation, a simple implementation that randomly decides a "possible" action.
     *
     * <p>Available on /bogo (e.g. localhost:8080/api/bogo)
     */
    @RequestMapping(value = "/bogo", method = RequestMethod.POST, produces = "application/json")
    public String bogoImplementation(@RequestBody Round round) {
        // Use "BogoSort" to select an action
        BogoImplementation bogo = new BogoImplementation();
        String action = bogo.selectAction(round);
        return action;
    }

    /**
     * VaccDeadlyFirstImplementation, an implementation that tries to defeat the deadly pathogen at
     * beginning and then develops vaccines for the other pathogens.
     *
     * <p>Available on /deadlyVacc (e.g. localhost:8080/api/deadlyVacc)
     */
    @RequestMapping(
            value = "/deadlyVacc",
            method = RequestMethod.POST,
            produces = "application/json")
    public String deadlyVaccImplmentation(@RequestBody Round round) {
        VaccDeadlyFirstImplementation impl = new VaccDeadlyFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * MedDeadlyFirstImplementation, an implementation that tries to defeat the deadly pathogen at
     * beginning and then develops medications for the other pathogens.
     *
     * <p>Available on /deadlyMed (e.g. localhost:8080/api/deadlyMed)
     */
    @RequestMapping(
            value = "/deadlyMed",
            method = RequestMethod.POST,
            produces = "application/json")
    public String deadlyMedImplementation(@RequestBody Round round) {
        MedDeadlyFirstImplementation impl = new MedDeadlyFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * VaccFastFirstImplementation, an implementation that tries to defeat the fastest pathogen at
     * beginning and then develops vaccines for the other pathogens.
     *
     * <p>Available on /fastVacc (e.g. localhost:8080/api/fastVacc)
     */
    @RequestMapping(value = "/fastVacc", method = RequestMethod.POST, produces = "application/json")
    public String fastVaccImplementation(@RequestBody Round round) {
        VaccFastFirstImplementation impl = new VaccFastFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * MedFastFirstImplementation, an implementation that tries to defeat the fastest pathogen at
     * beginning and then develops medications for the other pathogens.
     *
     * <p>Available on /fastMed (e.g. localhost:8080/api/fastMed)
     */
    @RequestMapping(value = "/fastMed", method = RequestMethod.POST, produces = "application/json")
    public String fastMedImplementation(@RequestBody Round round) {
        MedFastFirstImplementation impl = new MedFastFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * VaccSlowFirstImplementation, an implementation that tries to defeat the slowest pathogen at
     * beginning and then develops vaccines for the other pathogens.
     *
     * <p>Available on /slowVacc (e.g. localhost:8080/api/slowVacc)
     */
    @RequestMapping(value = "/slowVacc", method = RequestMethod.POST, produces = "application/json")
    public String slowVaccImplementation(@RequestBody Round round) {
        VaccSlowFirstImplementation impl = new VaccSlowFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * MedSlowFirstImplementation, an implementation that tries to defeat the slowest pathogen at
     * beginning and then develops medications for the other pathogens.
     *
     * <p>Available on /slowMed (e.g. localhost:8080/api/slowMed)
     */
    @RequestMapping(value = "/slowMed", method = RequestMethod.POST, produces = "application/json")
    public String slowMedImplementation(@RequestBody Round round) {
        MedSlowFirstImplementation impl = new MedSlowFirstImplementation();
        String action = impl.selectAction(round);
        return action;
    }

    /**
     * EndRoundImplementation, an implementation that only returns "endRound" for learning and
     * documentation purposes.
     *
     * <p>Available on /endRound (e.g. localhost:8080/api/endRound)
     */
    @RequestMapping(value = "/endRound", method = RequestMethod.POST, produces = "application/json")
    public String endRoundImplementation(@RequestBody Round round) {
        EndRoundImplementation impl = new EndRoundImplementation();
        String action = impl.selectAction(round);
        return action;
    }
}

/*
    // Examples, how views can also be written/created.

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

    @RequestMapping(
            value = "/endRound/{id}",
            method = RequestMethod.POST,
            produces = "application/json")
    public String endRoundImplementation(
            @PathVariable String id, @RequestBody Round round) {
        FileLoggerDecorator impl = new FileLoggerDecorator(EndRoundImplementation::new);
        impl.path = "/tmp/pandemieinc/" + id;
        String action = impl.selectAction(round);
        return action;
    }

*/
