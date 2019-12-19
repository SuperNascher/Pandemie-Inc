package de.tubs.pandemieinc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import de.tubs.pandemieinc.Strength;
import de.tubs.pandemieinc.City;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.Round;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.ActionHelper;
import java.util.ArrayList;

import de.tubs.pandemieinc.implementations.BogoImplementation;
import de.tubs.pandemieinc.implementations.VaccDeadlyFirstImplementation;
import de.tubs.pandemieinc.implementations.MedDeadlyFirstImplementation;
import de.tubs.pandemieinc.implementations.VaccFastFirstImplementation;
import de.tubs.pandemieinc.implementations.MedFastFirstImplementation;
import de.tubs.pandemieinc.implementations.VaccSlowFirstImplementation;
import de.tubs.pandemieinc.implementations.MedSlowFirstImplementation;

import de.tubs.pandemieinc.events.*;
import java.util.ArrayList;


// Delete me later
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;


@RestController
public class PandemieIncController {

    public int gameId = 5939;
    public ObjectMapper mapper = new ObjectMapper();
    public ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    public boolean activated = false;


    @RequestMapping(value="/dummy", method=RequestMethod.POST, produces="application/json")
    public String dummy(@RequestBody Round round) {


        // Return endRound, if game is not pending
        if (!round.outcome.equals("pending")) {
            return ActionPrinter.endRound();
        }

        // Use "BogoSort" to select an action
        BogoImplementation bogo = new BogoImplementation(round);
        String action = bogo.selectAction();
        return action;
    }

    @RequestMapping(value="/deadlyVacc", method=RequestMethod.POST, produces="application/json")
    public String deadlyVacc(@RequestBody Round round) {

        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;

        // Save the files

        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                VaccDeadlyFirstImplementation implement = new VaccDeadlyFirstImplementation(round);
                action = implement.selectAction();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                if (round.outcome.equals("win")) {
                    System.out.println("VaccDeadlyFirstImplementation Game: " +  Integer.toString(this.gameId) + ", Round: " + Integer.toString(round.round));
                }
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }
    
    @RequestMapping(value="/deadlyMed", method=RequestMethod.POST, produces="application/json")
    public String deadlyMed(@RequestBody Round round) {
        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;

        // Save the files

        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                MedDeadlyFirstImplementation implement = new MedDeadlyFirstImplementation(round);
                action = implement.selectAction();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                if (round.outcome.equals("win")) {
                    System.out.println("MedDeadlyFirstImplementation Game: " +  Integer.toString(this.gameId) + ", Round: " + Integer.toString(round.round));
                }
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }

    @RequestMapping(value="/fastVacc", method=RequestMethod.POST, produces="application/json")
    public String fastVacc(@RequestBody Round round) {
        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;

        // Save the files

        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                VaccFastFirstImplementation implement = new VaccFastFirstImplementation(round);
                action = implement.selectAction();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                if (round.outcome.equals("win")) {
                    System.out.println("VaccFastFirstImplementation Game: " +  Integer.toString(this.gameId) + ", Round: " + Integer.toString(round.round));
                }
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }

    @RequestMapping(value="/fastMed", method=RequestMethod.POST, produces="application/json")
    public String fastMed(@RequestBody Round round) {
        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;

        // Save the files

        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                MedFastFirstImplementation implement = new MedFastFirstImplementation(round);
                action = implement.selectAction();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                if (round.outcome.equals("win")) {
                    System.out.println("MedFastFirstImplementation Game: " +  Integer.toString(this.gameId) + ", Round: " + Integer.toString(round.round));
                }
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }

    @RequestMapping(value="/logging", method=RequestMethod.POST, produces="application/json")
    public String test(@RequestBody Round round) {
        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;

        // Save the files

        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                BogoImplementation bogo = new BogoImplementation(round);
                action = bogo.selectAction();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                if (round.outcome.equals("win")) {
                    System.out.println("BogoSort Game: " +  Integer.toString(this.gameId) + ", Round: " + Integer.toString(round.round));
                }
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }

    @RequestMapping(value="/loggingendround", method=RequestMethod.POST, produces="application/json")
    public String testendround(@RequestBody Round round) {
        String prePath = "/tmp/pandemieinc/";
        String directoryName = prePath.concat(Integer.toString(this.gameId));
        String action;


        // Save the files
        try {
            // Create game directory if not exists
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the round.json
            File roundFile = new File(directoryName + "/" + round.round + ".json");
            if (!roundFile.exists()) {
                roundFile.createNewFile();
                this.writer.writeValue(roundFile, round);
            }

            if (round.outcome.equals("pending")) {
                // Generate the action
                action = ActionPrinter.endRound();

                // Save round_response
                String actionFileName = directoryName + "/" + round.round + "_action.json";
                File roundActionFile = new File(actionFileName);
                boolean fileExists = roundActionFile.exists();
                RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
                if (fileExists) {
                    long position = roundActionWriter.length();
                    if (position > 0) {
                        roundActionWriter.seek(position - 2);
                    }
                    String actionFile = ",\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                } else {
                    String actionFile = "[\n" + action + "\n]";
                    roundActionWriter.write(actionFile.getBytes("UTF-8"));
                }
                roundActionWriter.close();
            } else {
                this.gameId = this.gameId + 1;
                action = ActionPrinter.endRound();
            }
        } catch (Exception e) {
            e.printStackTrace();
            action = ActionPrinter.endRound();
        }

        // Return the action
        return action;
    }


    @RequestMapping("/")
    public String index() {
        return "<html><head><title>Nothing to see here ;-)</title></head><body><h1>Hoi!</h1></body></html>";
    }
}
