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
 * Jans trained "selector implementation" network. + Chooses between MedDeadlyFirstImplementation,
 * MedFastFirstImplementation and MedSlowFirstImplementation.
 */
public class TriforceKIImplementation2 implements PandemieImpl {

    // "Neural Networks" / Matrix arrays
    INDArray who;
    INDArray wih1;
    INDArray wih2;
    INDArray bh1;
    INDArray bh2;
    INDArray bo;

    // Implementations that are used by the neural network
    MedDeadlyFirstImplementation medDeadlyFirstImplementation = new MedDeadlyFirstImplementation();
    MedFastFirstImplementation medFastFirstImplementation = new MedFastFirstImplementation();
    MedSlowFirstImplementation medSlowFirstImplementation = new MedSlowFirstImplementation();

    private static final Logger logger = LoggerFactory.getLogger(FileLoggerDecorator.class);
    // Used to indicate, if the import has worked or not
    private boolean importSuccess = false;

    public TriforceKIImplementation2() {
        try {
            Resource resource = new ClassPathResource("kernel3_0.csv");
            this.who = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
            resource = new ClassPathResource("bias3_0.csv");
            this.bo = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
            resource = new ClassPathResource("kernel_0.csv");
            this.wih1 = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
            resource = new ClassPathResource("bias_0.csv");
            this.bh1 = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
            resource = new ClassPathResource("kernel2_0.csv");
            this.wih2 = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
            resource = new ClassPathResource("bias2_0.csv");
            this.bh2 = Nd4j.readNumpy(resource.getFile().getPath(), ",").transpose();
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
        INDArray hiddenInputs1 = this.wih1.mmul(input);
        hiddenInputs1.add(bh1);
        INDArray hiddenOutputs1 = Transforms.elu(hiddenInputs1);
        INDArray hiddenInputs2 = this.wih2.mmul(hiddenOutputs1);
        hiddenInputs2.add(bh2);
        INDArray hiddenOutputs2 = Transforms.elu(hiddenInputs2);
        INDArray finalInputs = this.who.mmul(hiddenOutputs2);
        finalInputs.add(bo);

        // Get the results from the network
        INDArray finalOutputs = Transforms.softmax(finalInputs);
        finalOutputs = finalOutputs.tensorAlongDimension(0, 0);

        // Find the highest value
        int i = 1;
        for (int j = 2; j < 4; j++) {
            if (finalOutputs.getDouble(0, i) < finalOutputs.getDouble(0, j)) {
                i = j;
            }
        }

        // Return the action from the selected Implementation
        if (i == 1) {
            return this.medDeadlyFirstImplementation.selectAction(round);
        } else if (i == 2) {
            return this.medFastFirstImplementation.selectAction(round);
        } else if (i == 3) {
            return this.medSlowFirstImplementation.selectAction(round);
        }

        // "Default value", if something goes wrong
        return ActionPrinter.endRound();
    }
}
