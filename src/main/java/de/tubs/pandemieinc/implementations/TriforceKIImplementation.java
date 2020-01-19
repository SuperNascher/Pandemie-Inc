package de.tubs.pandemieinc.implementations;

import de.tubs.pandemieinc.ActionHelper;
import de.tubs.pandemieinc.ActionPrinter;
import de.tubs.pandemieinc.Pathogen;
import de.tubs.pandemieinc.Round;
import java.util.Collections;
import java.util.List;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Kevins trained "selector implementation" network. + Chooses between MedDeadlyFirstImplementation,
 * MedFastFirstImplementation and MedSlowFirstImplementation.
 */
public class TriforceKIImplementation implements PandemieImpl {

    // "Neural Networks" / Matrix arrays
    INDArray who;
    INDArray wih;

    // Implementations that are used by the neural network
    MedDeadlyFirstImplementation medDeadlyFirstImplementation = new MedDeadlyFirstImplementation();
    MedFastFirstImplementation medFastFirstImplementation = new MedFastFirstImplementation();
    MedSlowFirstImplementation medSlowFirstImplementation = new MedSlowFirstImplementation();

    private static final Logger logger = LoggerFactory.getLogger(FileLoggerDecorator.class);

    // Used to indicate, if the import has worked or not
    private boolean importSuccess = false;

    public TriforceKIImplementation() {
        try {
            Resource resource = new ClassPathResource("file_who.csv");
            this.who = Nd4j.readNumpy(resource.getFile().getPath(), ",");
            resource = new ClassPathResource("file_wih.csv");
            this.wih = Nd4j.readNumpy(resource.getFile().getPath(), ",");
            this.importSuccess = true;
        } catch (Exception e) {
            logger.error("Could not import model files for Neural Network.", e);
        }
    }

    public String selectAction(Round round) {
        if (!round.outcome.equals("pending") || this.importSuccess == false) {
            return ActionPrinter.endRound();
        }

        // Get the started pathogens and sort them
        List<Pathogen> pathogens = ActionHelper.getStartPathogens(round);
        Collections.sort(pathogens);

        // Create input array
        INDArray input = Nd4j.create(Pathogen.pathogensToNetwork(pathogens), new int[] {12, 1});

        // Apply with the trained "network"
        INDArray hiddenInputs = this.wih.mmul(input);
        INDArray hiddenOutputs = Transforms.sigmoid(hiddenInputs);
        INDArray finalInputs = this.who.mmul(hiddenOutputs);

        // Get the results from the network
        INDArray finalOutputs = Transforms.sigmoid(finalInputs);
        finalOutputs = finalOutputs.tensorAlongDimension(0, 0);

        // Find the highest value
        int i = 0;
        for (int j = 1; j < 3; j++) {
            if (finalOutputs.getDouble(0, i) < finalOutputs.getDouble(0, j)) {
                i = j;
            }
        }

        // Return the action from the selected Implementation
        if (i == 0) {
            return this.medDeadlyFirstImplementation.selectAction(round);
        } else if (i == 1) {
            return this.medFastFirstImplementation.selectAction(round);
        } else if (i == 2) {
            return this.medSlowFirstImplementation.selectAction(round);
        }

        // "Default value", if something goes wrong
        return ActionPrinter.endRound();
    }
}
