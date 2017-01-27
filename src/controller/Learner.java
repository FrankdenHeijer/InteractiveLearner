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
 * @author Merijn Kleinresingk & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Learner implements Protocol {

    /**
     *  Instance variables
     */
    private static boolean goOn = true;
    private static double total;
    private static double success;
//    private Classifier classifier;
//    private JFrameLearner jf;
    String prediction;

    /**
     * Simple test to test and learn the classifier
     *
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws IOException {
//        Classifier cl = new Classifier();
//        while (goOn) {
//            cl.train();
//           // learn(cl);
//        }
//        System.out.println("Thank you!");
//    }

    /*
     * The classifier can learn from input
     *
     * @param c
     * 		-	The classifier
     * @throws IOException
     */

    /**
     *
     * @throws IOException
     */

//    public void learn(Classifier c, JFrameLearner jf) throws IOException {
//        String input = "";
////        Scanner scanner = new Scanner(System.in);
//        //System.out.println("\nWelcome to the Interactive Learner!");
//        //System.out.println("What do you want to classify?");
//        //input = scanner.nextLine();
//        String predict = c.predict(jf.getClassifiable());
//        jf.getTextarea().setText("\nPrediction: " + predict + "\n");
//        if (input.equals("")) {
//            total += 1;
//            predict = c.predict(jf.getClassifiable());
//            String prediction = ("\nWe predict: " + predict + "\n Do you agree? (y/n)");
//            jf.predictionDialog(prediction);
////            if (jf.getPredictionBoolean()) {
////                success += 1;
////                
////                System.out.println("We will add this to our vocabulary");
////                String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
////                String classification = c.predict(jf.getClassifiable());
////                File newTextFile = new File(FILE_LOCATION + classification + "/" + timeLog + ".txt");
////
////                FileWriter fw = new FileWriter(newTextFile);
////                fw.write(jf.getClassifiable());
////                fw.close();
////            } else {
////                System.out.println("Which of the following classes do you think is right?");
////                ArrayList<String> list = new ArrayList<String>();
////
////                for (classes cl : classes.values()) {
////                    String className = cl.name();
////                    if (className != c.predict(jf.getClassifiable())) {
////                        System.out.println("- " + className);
////                        list.add(className);
////                    }
////                }
//  //              correct(list, input);
//    //        }
//        }
////        else {
////            System.out.println("This is not valid, please try again");
////        }
////        double acc = success / total * 100;
////        System.out.println("Accuracy = " + acc + "%");
////        System.out.println("Do you want to repeat? (y/n)");
////        if (!read()) {
////            goOn = false;
////        }
//    }
    public static void learn(Classifier c, JFrameLearner jf) throws IOException {
		String predict = c.predict(jf.getClassifiable());
                jf.predictionDialog(predict);
    }
    
    public void continueLearning(Classifier c, JFrameLearner jf, Boolean predictionBoolean) throws IOException {
            if(predictionBoolean) {
                success += 1;
                jf.appendToTextarea("We will add this to our vocabulary\n");
                String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                String classification = c.predict(jf.getClassifiable());
                File newTextFile = new File(FILE_LOCATION + classification + "/" + timeLog + ".txt");

                FileWriter fw = new FileWriter(newTextFile);
                fw.write(jf.getClassifiable());
                fw.close();
                jf.appendToTextarea("You can add a new document to train or you can test the learner by pressing the corresponding buttons\n");
                }
                 else {
                jf.falsePredictionDialog();
            }
    }
    
    public void correctedLearning(JFrameLearner jf, String in) throws IOException {
                success += 1;
                jf.appendToTextarea("We will add this to our vocabulary");
                String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                File newTextFile = new File(FILE_LOCATION + in + "/" + timeLog + ".txt");
                FileWriter fw = new FileWriter(newTextFile);
                fw.write(jf.getClassifiable());
                fw.close();
                jf.appendToTextarea("You can add a new document to train or you can test the learner by pressing the corresponding buttons\n");
                }
}