package controller;

import java.io.*;
import java.util.Scanner;
import model.Classifier;
import view.JFrameLearner;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleinresingk & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Tester implements Protocol {
    /**
    * Instance Variables
    */
    static double successes = 0;
    static double failures = 0;
//    private double total;


    /**
     *Main method for tester class. Actual testing will happen here
     *
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws IOException {
//        Classifier cl = new Classifier(20);
//        cl.train();
//        for(classes c : classes.values()){
//            String className = c.name();
//            measure(cl, TEST_LOCATION, className);
//        }
//    }
    
    /**
     * Measure method which will measure the accurateness of the classifier.
     * @param c
     *          - Classifier instance.
     * @param place
     *          - Shows path to the file location.
     * @name
     *          - Name of the tested class.
     */
    public static void measure(Classifier c, String directoryLocation, String ClassName, JFrameLearner jf) throws FileNotFoundException, IOException {
        File location = new File(directoryLocation);
        System.out.println(location.listFiles().length);
        for(int i = 0; i < location.listFiles().length;) {
           File[] files = location.listFiles();
           BufferedReader r = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
           String content = "";
           while(r.readLine() != null) {
               content += r.readLine();
           }
           r.close();
           String predict = c.predict(content);
           System.out.println(ClassName);
           if(predict.equals(ClassName)) {
               successes ++;
           }
           else{
               failures ++;
           }
           System.out.println(successes + " " + failures);
           i++;
           }
        System.out.println("s: " + successes + " f: " + failures );
        jf.clearTextarea();
        jf.appendToTextarea("Successes: " + successes + "\nFailures: " + failures + "\nAccuracy: " + successes/(successes+failures));
        successes = 0;
        failures = 0;
        }
    }


