package model;

import controller.Protocol;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;

import static java.lang.Math.*;

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
    private HashMap<String, HashMap<String, Integer>> vocabulary;
    private double totalNumberOfDocs;
    private HashMap<String,Double> numberOfDocsPerClass;
    private Map<String, Double> chiMap = new HashMap<>();
    private HashMap<String,Integer> numberOfWordsPerClass;
    private final double criticalValue = 10.83;
    private int featureSize;
    private int numberOfDistinctWords;

    /**
     *  Classifier constructor
     */
    public Classifier(int featureSize) {
        this.featureSize = featureSize;
        vocabulary = new HashMap<String, HashMap<String, Integer>>();
        numberOfDocsPerClass = new HashMap<String, Double>();
        numberOfWordsPerClass = new HashMap<String, Integer>();

    }

    /**
     * Train the model (vocabulary) with the given training set
     *
     * @throws IOException
     */
    public void train() throws IOException {
        for (classes c : classes.values()) {
            String className = c.name();
            vocabulary.put(className, new HashMap<String, Integer>());
            String location = FILE_LOCATION + className;
            File folder = new File(location);
            openFolder(folder, className);
        }
        selectFeatures();

//        getNumberOfWordsPerClass();
        double counter = 0;
        for(String c: numberOfDocsPerClass.keySet()){
            counter += numberOfDocsPerClass.get(c);
        }
        this.numberOfDistinctWords = getDistinctWords().size();
        totalNumberOfDocs = counter;
        chiMap = sortChiMap(calcChiSquare());
        removeLowChiSquares();
    }

    /**
     * Sorts all chi square values in descending order
     */
    public Map<String, Double> sortChiMap(Map<String, Double> chiMap) {
        Object[] a = chiMap.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Double>) o2).getValue()
                        .compareTo(((Map.Entry<String, Double>) o1).getValue());
            }
        });
        Map<String, Double> returnMap = new HashMap<String, Double>();
        int i = featureSize;
        for (Object e : a) {
            if (i > 0) {
                returnMap.put(((Map.Entry<String, Double>) e).getKey(), ((Map.Entry<String, Double>) e).getValue());
                i--;
            }
        }
        System.out.println("\nChi Square values sorted\n");
        return returnMap;
    }


    /**
     * Counts the total amount of words for each class
     */
    public int getNumberOfWordsPerClass(String className){
        int words = 0;
        Map<String, Integer> wordsPerClas = vocabulary.get(className);
        for (Integer value : wordsPerClas.values()) {
            words += value;
        }
        return words;
    }

    /**
     * Returns the all unique words of the classes combined
     */
    public List<String> getDistinctWords() {
        ArrayList<String> distinctWords = new ArrayList<String>();
        for (classes c : classes.values()) {
//            String classname = c.name();
            HashMap<String, Integer> subSet = vocabulary.get(c.name());
            for (String word : subSet.keySet()) {
                if (!distinctWords.contains(word)) {
                    distinctWords.add(word);
                }
            }
        }
        return distinctWords;
    }

    public String[] getDistinctWords(String[] tokenized) {
        List<String> distinctWords = new LinkedList<String>();
        for (String word : tokenized) {
            if (!distinctWords.contains(word)) {
                distinctWords.add(word);
            }
        }
        return distinctWords.toArray(new String[distinctWords.size()]);
    }

    /**
     * Fills the vocabulary model with the training data
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
                    HashMap<String, Integer> subSet = vocabulary.get(className);
                    if (subSet.containsKey(feature)) {
                        subSet.put(feature, subSet.get(feature) + 1);
                    } else {
                        subSet.put(feature, 1);
                    }
                    vocabulary.put(className, subSet);
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
    public double getProbability(String className, String feature, double count) {
        double denominator = (count + (SMOOTHING_K * this.numberOfDistinctWords));
        double probability = 0.0;
        if (vocabulary.get(className).get(feature) != null) {
            double counter = new Integer(vocabulary.get(className).get(feature)).doubleValue() + SMOOTHING_K;
            probability = (counter) / (denominator);
        } else {

            probability = (SMOOTHING_K) / (denominator);
        }
        return probability;
    }

    /**
     * Calculates the Chi square value of all the words in the vocabulary
     * @return A Map of all the words with their respective chi square value.
     */
    public Map<String, Double> calcChiSquare() {
        for (String className : vocabulary.keySet()) {
            numberOfWordsPerClass.put(className, getNumberOfWordsPerClass(className));
        }
        List<String> distinctWords = getDistinctWords();

        Map<String, Double> chiSquareMapping = new HashMap<>();
        for (String word : distinctWords) {
            double chiSquare = calcChiSquare(word);
            chiSquareMapping.put(word, chiSquare);
        }
        System.out.println("\nChi Square values calculated\n");
        return chiSquareMapping;
    }

    public Double calcChiSquare(String word) {
        double chiSquare = 0;
        Set<String> classSet = vocabulary.keySet();
        for (String className : classSet) {
            numberOfWordsPerClass.put(className, getNumberOfWordsPerClass(className));
        }
        List<List<Double>> wordCounts = new LinkedList<>();
        List<Double> classCounts = new LinkedList<>();
        double w1 = 0;
        double w2 = 0;
        double N = 0;
        for (String className : classSet) {
            double w = 0;
            if (vocabulary.get(className).get(word) != null) {
                w = vocabulary.get(className).get(word);
            } else {
                w = 0;
            }
            w1 += w;
            double c = numberOfWordsPerClass.get(className);
            classCounts.add(c);
            N += c;
            double notW = c-w;
            w2 += notW;
            LinkedList<Double> tuple = new LinkedList<>();
            tuple.add(w);
            tuple.add(notW);
            wordCounts.add(tuple);
        }
        // Calculate the expected values
        List<List<Double>> eValues = new LinkedList<List<Double>>();
        for (double j : classCounts) {
            LinkedList<Double> tuple = new LinkedList<Double>();
            tuple.add((w1*j)/N);
            tuple.add((w2*j)/N);
            eValues.add(tuple);
        }
        // Calculate the Chi-Square values
        for (int i = 0 ; i < eValues.size(); i++) {
            chiSquare += pow(wordCounts.get(i).get(0)-eValues.get(i).get(0), 2)/eValues.get(i).get(0);
            chiSquare += pow(wordCounts.get(i).get(1)-eValues.get(i).get(1), 2)/eValues.get(i).get(1);
        }
        return chiSquare;
    }

    /**
     * Removes the words from the vocabulary that have a value lower than the critical value set.
     * Because if they have a Chi square value lower than the critical value, they are not unique enough for a given class
     */
    public void removeLowChiSquares() {
        Map<String, Double> chiMapping = chiMap;
        for (String className : vocabulary.keySet()) {
            int i = 0;
            Set<String> wordSet = vocabulary.get(className).keySet();
            Iterator<String> iterator = wordSet.iterator();
            while(iterator.hasNext()) {
                String word = iterator.next();
                if (chiMapping.get(word) != null && chiMapping.get(word) < criticalValue) {
                    iterator.remove();
                    i++;
                }
            }
            System.out.println("\nChi Square below critical value removed from vocaulary\n");
            System.out.println("\nThe word list for class " + className + " consists of " + getNumberOfWordsPerClass(className));
            System.out.println("\nAnd has " + vocabulary.get(className).keySet().size() + " different words");
        }

    }

    /**
     * This method classifies the given document in a class
     * @param document - the document that has to be classified
     * @return - The classification of the document
     */
    public String predict(String document){
        HashMap<String,Double> results = new HashMap<>();
        String[] featureVector = prepare(document);

        for(classes c : classes.values()) {
            String className = c.name();
            double probabilityClass = 0.0;
            double count = getNumberOfWordsPerClass(className);
            for(String feature : featureVector) {
                if (chiMap.containsKey(feature)) {
                    probabilityClass += Math.log(getProbability(className, feature, count))/Math.log(2);
                }
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
            HashMap<String, Integer> subSet = vocabulary.get(className);
            HashMap<String, Integer> newSubSet = new HashMap<String, Integer>();
            for (String s : subSet.keySet()){
                int occurrence = subSet.get(s);
                if (occurrence > FEATURE_THRESHOLD) {
                    newSubSet.put(s, occurrence);
                }
            }
            vocabulary.put(className, newSubSet);
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

        String[] uniqueWords = getDistinctWords(tokenized);

        String[] noStopWords = removeStopwords(uniqueWords);
        return noStopWords;
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
    
    public String[] removeStopwords(String[] tokens) {
        int count = 0;
        List<String> strings = new LinkedList<String>(Arrays.asList(tokens));
        for (int j = 0; j < stopwords.length; j++) {
            if (strings.contains(stopwords[j])) {
                count++;
                strings.remove(stopwords[j]);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }
}