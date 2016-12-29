package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Frank and Merijn on 20-12-16.
 */
public class Classifier implements controller.Protocol {

    private HashMap<String, HashMap<String, Integer>> bagOfWords;
    private double distinctWords;
    private HashMap<String,Double> docsPerClass;
    private HashMap<String,Double> totalWordsPerClass;
    private double aantalDocsTotaal;


    public Classifier() {
        bagOfWords = new HashMap<String, HashMap<String, Integer>>();
        totalWordsPerClass = new HashMap<String, Double>();
        docsPerClass = new HashMap<String, Double>();
    }

    /**
     * Counts the distinct amount of words of the classes combined
     */
    public void distinctWords() {
        ArrayList<String> features = new ArrayList<String>();
        for (classes c : classes.values()) {
            String classname = c.name();
            HashMap<String, Integer> subSet = bagOfWords.get(classname);
            for (String s : subSet.keySet()) {
                if (!features.contains(s)) {
                    features.add(s);
                }
            }
        }
        distinctWords = new Integer(features.size()).doubleValue();
    }
}
