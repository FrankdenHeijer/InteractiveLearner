package controller;

import java.io.*;
import java.util.Scanner;
import model.Classifier;
import view.JFrameLearner;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleinreesink & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Tester implements Protocol {
    /**
    * Instance Variables
    */
    static double successes = 0;
    static double failures = 0;
    
    /**
     * Measure function which will determine the accuracy of the trained classifier.
     * @param c
     * @param directoryLocation
     * @param Class
     * @param jf
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void measure(Classifier c, String directoryLocation, String Class, JFrameLearner jf) throws FileNotFoundException, IOException {
        File loc = new File(directoryLocation);
        for(int i = 1; i < loc.listFiles().length;) {
           File[] files = loc.listFiles();
           BufferedReader r = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
           String text = "";
           while(r.readLine() != null) {
               text += r.readLine();
           }
           r.close();
           String pString = c.predictClass(text);
           System.out.println(Class);
           if(pString.equals(Class)) {
               successes = successes + 1;
           }
           else{
               failures = failures + 1;
           }
           i++;
           }
        jf.clearTextarea();
        jf.appendToTextarea("Successes: " + successes + "\nFailures: " + failures + "\nAccuracy: " + successes/(successes+failures));
        successes = 0;
        failures = 0;
        }
    }


