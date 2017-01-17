package controller;

import model.Classifier;
import view.JFrameLearner;

import java.io.IOException;

/**
 * Created by Frank on 17-01-17.
 */
public class InteractiveLearner implements Protocol {

    /**
     *  Instance variables
     */
    private static boolean goOn = true;

    /**
     * Simple test to test and learn the classifier
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Classifier classifier = new Classifier();
        JFrameLearner jf = new JFrameLearner();
        Learner learner = new Learner();
        jf.setLearner(learner);
        jf.setClassifier(classifier);
        classifier.train();
        jf.setVisible(true);
    }

    public void setGoOn(boolean goOn) {
        this.goOn = goOn;
    }
}
