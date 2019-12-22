package de.tubs.pandemieinc.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tubs.pandemieinc.Round;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLoggerDecorator extends ImplementationDecorator {

    public String path;
    public String roundActionFilePattern = "%d_action.json";
    public String roundFilePattern = "%d.json";

    private static final Logger logger = LoggerFactory.getLogger(FileLoggerDecorator.class);

    public FileLoggerDecorator(PandemieImpl implementation) {
        super(implementation);
    }

    public FileLoggerDecorator(Supplier<PandemieImpl> constructor) {
        super(constructor);
    }

    public void setupFileLoggin() {
        // Check path/directory
        File directory = new File(this.path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public String selectAction(Round round) {

        this.setupFileLoggin();
        // Save the Round
        Path roundPath = Paths.get(this.path, String.format(this.roundFilePattern, round.round));
        try {
            // Save the round object as JSON
            ObjectMapper mapper = new ObjectMapper();
            String roundAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(round);
            OutputStreamWriter roundFile =
                    new OutputStreamWriter(new FileOutputStream(roundPath.toFile()), "UTF-8");
            roundFile.write(roundAsJson);
            roundFile.flush();
            roundFile.close();
        } catch (JsonProcessingException e) {
            logger.error("Could not generate JSON from round.", e);
        } catch (java.io.IOException e) {
            logger.error("Could not create file: " + roundPath, e);
        }

        // Get the action from the Implementation
        String action = super.selectAction(round);

        // Skip action logging, if the game is over
        if (!round.outcome.equals("pending")) {
            return action;
        }

        // Save the action
        Path roundActionPath =
                Paths.get(this.path, String.format(roundActionFilePattern, round.round));
        File roundActionFile = roundActionPath.toFile();
        boolean fileExists = roundActionFile.exists();
        try {
            RandomAccessFile roundActionWriter = new RandomAccessFile(roundActionFile, "rw");
            if (fileExists) {
                // Overwrite and append onto the round action file
                long position = roundActionWriter.length();
                if (position > 0) {
                    roundActionWriter.seek(position - 2);
                }
                String actionFile = ",\n" + action + "\n]";
                roundActionWriter.write(actionFile.getBytes("UTF-8"));
            } else {
                // Write as a new file
                String actionFile = "[\n" + action + "\n]";
                roundActionWriter.write(actionFile.getBytes("UTF-8"));
            }
            roundActionWriter.close();
        } catch (JsonProcessingException e) {
            logger.error("Could not save action.", e);
        } catch (java.io.IOException e) {
            logger.error("Could not create file: " + roundActionPath, e);
        }
        return action;
    }
}
