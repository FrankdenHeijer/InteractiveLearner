package model;

import controller.Protocol;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleinresingk & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Classifier implements Protocol {

    /**
     *  Instance variables
     */
    private HashMap<String, HashMap<String, Integer>> allWords;
    private double totalNumberOfDocs;
    private HashMap<String,Double> numberOfDocsPerClass;
//    private double distinctWords;
//    private HashMap<String,Double> numberOfWordsPerClass;

    /**
     *  Classifier constructor
     */
    public Classifier() {
        allWords = new HashMap<String, HashMap<String, Integer>>();
        numberOfDocsPerClass = new HashMap<String, Double>();
//        numberOfWordsPerClass = new HashMap<String, Double>();

    }

    /**
     * Train the model (allWords) with the given training set
     *
     * @throws IOException
     */
    public void train() throws IOException {
        for (classes c : classes.values()) {
            String className = c.name();
            allWords.put(className, new HashMap<String, Integer>());
            String location = FILE_LOCATION + className;
            File folder = new File(location);
            openFolder(folder, className);
        }
        selectFeatures();
        getNumberOfWordsPerClass();
        double counter = 0;
        for(String c: numberOfDocsPerClass.keySet()){
            counter += numberOfDocsPerClass.get(c);
        }
        totalNumberOfDocs = counter;
    }

    /**
     * Counts the total amount of words for each class
     */
    public HashMap<String,Double> getNumberOfWordsPerClass(){
        HashMap<String,Double> temp = new HashMap<String,Double>();
        for(classes c : classes.values()){
            double counter = 0;
            String className = c.name();
            for(int v : allWords.get(className).values()) {
                counter = counter + new Integer(v).doubleValue();
            }
            temp.put(className, counter);

        }
        return temp;
    }

    /**
     * Returns the distinct amount of words of the classes combined
     */
    public double getDistinctWordsCount() {
        ArrayList<String> features = new ArrayList<String>();
        for (classes c : classes.values()) {
            String classname = c.name();
            HashMap<String, Integer> subSet = allWords.get(classname);
            for (String s : subSet.keySet()) {
                if (!features.contains(s)) {
                    features.add(s);
                }
            }
        }
        return new Integer(features.size()).doubleValue();
    }

    /**
     * Fills the allWords model with the training data
     *
     * @param folder
     *            - The folder where you can find the training data
     * @param className
     *            - The name of the class
     * @throws IOException
     */
    public void openFolder(File folder, String className) throws IOException {
        double numberOfFiles = 0;
        for (File entry : folder.listFiles()) {
            if (entry.isDirectory()) {
                openFolder(entry, className);
            } else {
                numberOfFiles += 1;
                BufferedReader reader = new BufferedReader(new FileReader(entry.getAbsolutePath()));
                String content = "";
                while (reader.readLine() != null) {
                    content += reader.readLine();
                }
                reader.close();
                String[] featureVector = prepare(content);
                for (int i = 0; i < featureVector.length; i++){
                    String feature = featureVector[i];
                    HashMap<String, Integer> subSet = allWords.get(className);
                    if (subSet.containsKey(feature)) {
                        subSet.put(feature, subSet.get(feature) + 1);
                    } else {
                        subSet.put(feature, 1);
                    }
                    allWords.put(className, subSet);
                }
            }
        }
        System.out.println("DOCS_TRAINED_PER_CLASS " + className +": " + numberOfFiles);
        numberOfDocsPerClass.put(className, numberOfFiles);
    }

    /**
     * This method calculates the probability of each word in a class
     * @param className
     *            - The name of the class
     * @param feature
     *            - A word in a class where you have to calculate the
     *            probability
     * @return
     */
    public double getProbability(String className, String feature){
        double count = getNumberOfWordsPerClass().get(className);
        double denominator = (count + (SMOOTHING_K * getDistinctWordsCount()));
        double probability = 0.0;
        if (allWords.get(className).get(feature) != null) {
            double counter = new Integer(allWords.get(className).get(feature)).doubleValue() + SMOOTHING_K;
            probability = (counter) / (denominator);
        } else {

            probability = (SMOOTHING_K) / (denominator);
        }
        return probability;
    }

    /**
     * This method classifies the given document in a class
     * @param document - the document that has to be classified
     * @return - The classification of the document
     */
    public String predict(String document){
        HashMap<String,Double> results = new HashMap<String,Double>();
        String[] featureVector = prepare(document);
        for(classes c : classes.values()) {
            String className = c.name();
            double probabilityClass = 0.0;
            for(String feature : featureVector) {
                probabilityClass += Math.log10(getProbability(className, feature))/Math.log10(2);

            }
            double probabilityOfClass = ((numberOfDocsPerClass.get(className)) / (totalNumberOfDocs));
            double probability = probabilityClass + ((Math.log10(probabilityOfClass)) / (Math.log10(2)));
            results.put(className, probability);
        }
        double currentMax = 0.0;
        String firstResult = (String)results.keySet().toArray()[0];
        currentMax = results.get(firstResult);
        String classified = firstResult;
        for (String c : results.keySet()){
            if (currentMax < results.get(c)){
                currentMax = results.get(c);
                classified = c;
            }
        }
        return classified;
    }

    /**
     * Checks if occurrence of a word is higher than the threshold.
     * When this is not the case, the word will not be included
     */
    public void selectFeatures() {
        for (classes c : classes.values()) {
            String className = c.name();
            HashMap<String, Integer> subSet = allWords.get(className);
            HashMap<String, Integer> newSubSet = new HashMap<String, Integer>();
            for (String s : subSet.keySet()){
                int occurrence = subSet.get(s);
                if (occurrence > FEATURE_THRESHOLD) {
                    newSubSet.put(s, occurrence);
                }
            }
            allWords.put(className, newSubSet);
        }
    }

    /**
     *
     * @param document - A document that has to be set in to a vector.
     * @return - The tokenized and normalized string array
     */
    public String[] prepare(String document) {
        String normalized = normalize(document);
        String[] tokenized = tokenize(normalized);
        return tokenized;
    }

    /**
     *
     * @param document - A document that has to be normalized
     * @return - The normalized document
     */
    public String normalize(String document) {
        document = document.toLowerCase();
        document = document.trim();
        return document == null ? null : document.replaceAll("[^a-zA-Z\\d\\s]", "");
    }

    /**
     *
     * @param document - A document that has to be tokenized
     * @return - A tokenized document
     */
    public String[] tokenize(String document) {
        return document.split(" ");
    }
}