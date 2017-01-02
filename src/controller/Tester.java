package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import model.Classifier;

/**
 * Tester class for the Interactive Learner.
 * The class will determine a score based on the used classifier and a given dataset.
 */


public class Tester implements Protocol {
    /**
    * Instance Variables
    */
    private double succeses;
    private double failures;
    private double total;
    
    /**
    * Constructor of the tester class
    * @param c
    *           - Classifier instance
    */
    public Tester(Classifier c ) {
        
    }
    
    /**
     * Main method for tester class. Actual testing will happen here
     * @param args 
     */
    public static void main(String[] args) {
        
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
    public void measure(Classifier c, String place, String name) throws FileNotFoundException {
        File location = new File(place + name);
        for(int i = 0; i < location.listFiles().length; i++) {
           File[] files = location.listFiles();
           if(files[i].isDirectory()) {
               measure(c, place, name);
           }
           else{
               total ++;
               BufferedReader r = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
           }
        }
    }
   


}
