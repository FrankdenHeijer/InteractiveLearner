package controller;

import java.io.*;
import java.util.Scanner;
import model.Classifier;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleinresingk & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Tester implements Protocol {

    /**
     *Main method for tester class. Actual testing will happen here
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Classifier cl = new Classifier();
        cl.train();
        for(classes c : classes.values()){
            String className = c.name();
            measure(cl, TEST_LOCATION, className);
        }
    }
    
    /**
     * Measure method which will measure the accurateness of the classifier.
     * @param c
     *          - Classifier instance.
     * @param place
     *          - Shows path to the file location.
     * @name
     *          - Name of the tested class.
     */
    public static void measure(Classifier c, String place, String name) throws IOException {
        File location = new File(place + name);
        double total = 0;
        double successes = 0;
        double failures = 0;
        for(int i = 0; i < location.listFiles().length; i++) {
           File[] files = location.listFiles();
           if(files[i].isDirectory()) {
               measure(c, place, name);
           }
           else{
               total ++;
               BufferedReader r = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
               String content = "";
               while (r.readLine() != null) {
                   content += r.readLine();
               }
               r.close();
               String predict = c.predict(content);
               if(predict.equals(name)){
                   successes += 1;
               } else {
                   failures += 1;
               }
           }
        }
        System.out.println("\n================" + name + "================");
        System.out.println("Successes: " + successes);
        System.out.println("Failures:  " + failures);
        System.out.println("-----------------");
        System.out.println("Accuracy : "+ (successes / total) * 100 + "%");
        System.out.println("====================================");
    }
}