package controller;

import model.Classifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import view.JFrameLearner;

import javax.swing.*;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleinreesink & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Learner implements Protocol {

    /**
     *  Instance variables
     */
    String prediction;
    File file = new File(FILE_LOCATION);
    
    /**
     * First phase of learning the classifier a new document. Instantized out of JFrameLearner, will try to predict the given content.
     * @param cl
     * @param jf
     * @throws IOException 
     */
    public static void learn(Classifier cl, JFrameLearner jf) throws IOException {
		String prediction = cl.predictClass(jf.getContent());
                System.out.println(cl.predictClass(jf.getContent()));
                jf.predictionDialog(prediction);
    }
    
    /**
     * Second phase of learning the classifier a new document. It will be checked if the prediction was right, otherwise give the input manaual.
     * @param cl
     * @param jf
     * @param predictionBoolean
     * @throws IOException 
     */
    public void continueLearning(Classifier cl, JFrameLearner jf, Boolean predictionBoolean) throws IOException {
            if(predictionBoolean) {
                jf.appendToTextarea("The file will be added to the corpus\n");
                int count = file.listFiles().length + 1;
                String textName = "ManualTest" + count;
                String classification = cl.predictClass(jf.getContent());
                //If prediction is alright, add a new file to the corpus
                File newFile = new File(FILE_LOCATION + classification + "/" + textName + ".txt");

                FileWriter fw = new FileWriter(newFile);
                fw.write(jf.getContent());
                fw.close();
                jf.appendToTextarea("You can add a new document to train or you can test the learner by pressing the corresponding buttons\n");
                }
                 else {
                jf.falsePredictionDialog();
            }
    }
    /**
     * Third phase of learning the classifier a new document. If the prediction wasn't right, and the manual class is. Then continue process.
     * @param jf
     * @param in
     * @throws IOException 
     */
    public void correctedLearning(JFrameLearner jf, String in) throws IOException {
                jf.appendToTextarea("We will add this to our vocabulary");
                int count = file.listFiles().length + 1;
                String textName = "ManualTest" + count;
                //If prediction is alright, add a new file to the corpus
                File newFile = new File(FILE_LOCATION + in + "/" + textName + ".txt");
                FileWriter fw = new FileWriter(newFile);
                fw.write(jf.getContent());
                fw.close();
                jf.appendToTextarea("You can add a new document to train or you can test the learner by pressing the corresponding buttons\n");
                }
}