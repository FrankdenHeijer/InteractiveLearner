package model;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import controller.Protocol;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;

import static java.lang.Math.*;

/*
 * MOD 6 - Intelligent Interaction Design
 * AI Project: Interactive Learner
 *
 * @author Merijn Kleeinresink & Frank den Heijer
 * Created on: 20-12-2016
 */
public class Classifier implements Protocol {

    /**
     *  Instance variables
     */
    private HashMap<String, HashMap<String, Integer>> vocabulary;
    private HashMap<String,Double> numberOfDocsPerClass;
    private Map<String, Double> chiSquareMap = new HashMap<>();
    private HashMap<String,Integer> numberOfWordsPerClass;
    private int featureSize;
    private int numberOfDistinctWords;

    /**
     *  Instructor for Classifier
     */
    public Classifier(int featureSize) {
        this.featureSize = featureSize;
        vocabulary = new HashMap<String, HashMap<String, Integer>>();
        numberOfDocsPerClass = new HashMap<String, Double>();
        numberOfWordsPerClass = new HashMap<String, Integer>();
    }

    /**
     * Train the classifier
     *
     * @throws IOException
     */
    public void train() throws IOException {
        for (classes c : classes.values()) {
            vocabulary.put(c.name(), new HashMap<String, Integer>());
            String location = FILE_LOCATION + c.name();
            File folder = new File(location);
            openFolder(folder, c.name());
        }
        applyThreshold();
        this.numberOfDistinctWords = getDistinctWords().size();
        chiSquareMap = sortChiMap(calcChiSquare());
        removeLowChiSquares();
    }

    /**
     * Sorts all chi square values in descending order
     * and returns the ordered map
     *
     * @param chiMap
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
        System.out.println("LOG: Chi Square values sorted\n");
        return returnMap;
    }


    /**
     * Returns total number of documents
     */
    public double totalNumberOfDocs() {
        double counter = 0.0;
        for(String c: numberOfDocsPerClass.keySet()){
            counter += numberOfDocsPerClass.get(c);
        }
        return counter;
    }

    /**
     * Counts the total number of words for each class
     */
    public int getNumberOfWordsPerClass(String className){
        int words = 0;
        Map<String, Integer> wordsPerClas = vocabulary.get(className);
        for (Integer count : wordsPerClas.values()) {
            words += count;
        }
        return words;
    }

    /**
     * Returns all unique words of all the classes
     */
    public List<String> getDistinctWords() {
        ArrayList<String> distinctWords = new ArrayList<String>();
        for (classes c : classes.values()) {
//            String classname = c.name();
            HashMap<String, Integer> classMap = vocabulary.get(c.name());
            for (String word : classMap.keySet()) {
                if (!distinctWords.contains(word)) {
                    distinctWords.add(word);
                }
            }
        }
        return distinctWords;
    }

    /**
     * Removes all duplicate values from a String[]
     *
     * @param tokenized
     */
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
     * Adds all files from a folder to the vocabulary
     *
     * @param folder
     *
     * @param className
     *
     * @throws IOException
     */
    public void openFolder(File folder, String className) throws IOException {
        double numberOfFiles = 0;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                openFolder(file, className);
            } else {
                numberOfFiles ++;
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String content = "";
                while (reader.readLine() != null) {
                    content += reader.readLine();
                }
                reader.close();
                String[] features = prepare(content);
                for (int i = 0; i < features.length; i++){
                    HashMap<String, Integer> subSet = vocabulary.get(className);
                    if (subSet.containsKey(features[i])) {
                        subSet.put(features[i], subSet.get(features[i]) + 1);
                    } else {
                        subSet.put(features[i], 1);
                    }
                    vocabulary.put(className, subSet);
                }
            }
        }
        System.out.println(numberOfFiles + " documents trained for " + className);
        numberOfDocsPerClass.put(className, numberOfFiles);
    }

    /**
     * Calculates the probability for each word in a class
     *
     * @param className
     *
     * @param feature
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
     *
     * @return A Map of all the words with their respective chi square value.
     */
    public Map<String, Double> calcChiSquare() {
//        for (String className : vocabulary.keySet()) {
//            numberOfWordsPerClass.put(className, getNumberOfWordsPerClass(className));
//        }
        List<String> distinctWords = getDistinctWords();

        Map<String, Double> chiSquareMapping = new HashMap<>();
        for (String word : distinctWords) {
            double chiSquare = calcChiSquare(word);
            chiSquareMapping.put(word, chiSquare);
        }
        System.out.println("LOG: Chi Square values calculated\n");
        return chiSquareMapping;
    }

    /**
     * Calculate the Chi square value of a single word
     */
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
     * The critical value determines if a word is unique enough to be used in the vocabulary
     */
    public void removeLowChiSquares() {
        Map<String, Double> chiMapping = chiSquareMap;
        for (String className : vocabulary.keySet()) {
            int i = 0;
            Set<String> wordSet = vocabulary.get(className).keySet();
            Iterator<String> iterator = wordSet.iterator();
            while(iterator.hasNext()) {
                String word = iterator.next();
                if (chiMapping.get(word) != null && chiMapping.get(word) < CRITICAL_VALUE) {
                    iterator.remove();
                    i++;
                }
            }
            System.out.println("LOG: Chi Square values below critical value removed from vocaulary\n");
            System.out.println("LOG: The word list for class " + className + " consists of " + getNumberOfWordsPerClass(className));
            System.out.println("LOG: And has " + vocabulary.get(className).keySet().size() + " different words\n\n");
        }

    }

    /**
     * This method classifies the given document in a class
     *
     * @param document - the document that has to be classified
     */
    public String predictClass(String document){
        HashMap<String,Double> predictedClass = new HashMap<>();
        String[] features = prepare(document);
        double totalNumberOfDocs = totalNumberOfDocs();
        // calcultate probabilities per class
        for(classes c : classes.values()) {
            String className = c.name();
            double probabilityClass = 0.0;
            double count = getNumberOfWordsPerClass(className);
            for (String feature : features) {
                // calculate probability if features exist in chi squared mapping
                if (chiSquareMap.containsKey(feature)) {
                    probabilityClass += Math.log(getProbability(className, feature, count)) / Math.log(2);
                }
            }
            double probabilityOfClass = ((numberOfDocsPerClass.get(className)) / (totalNumberOfDocs));
            double probability = probabilityClass + ((Math.log10(probabilityOfClass)) / (Math.log10(2)));
            predictedClass.put(className, probability);
        }

        // select class with hightes probability
        double max = 0.0;
        String highestProb = (String)predictedClass.keySet().toArray()[0];
        max = predictedClass.get(highestProb);
        String classified = highestProb;
        for (String c : predictedClass.keySet()){
            if (max < predictedClass.get(c)){
                max = predictedClass.get(c);
                classified = c;
            }
        }
        return classified;
    }

    /**
     * Only allow words above the threshold
     */
    public void applyThreshold() {
        for (classes c : classes.values()) {
            HashMap<String, Integer> classMap = vocabulary.get(c.name());
            HashMap<String, Integer> subSet = new HashMap<String, Integer>();
            for (String s : classMap.keySet()){
                int count = classMap.get(s);
                if (count > FEATURE_MIN && count < FEATURE_MAX) {
                    subSet.put(s, count);
                }
            }
            vocabulary.put(c.name(), subSet);
        }
    }

    /**
     * Prepare a documents to be classified.
     *
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

    /**
     * Removes often occurring words as determined in Protocol
     *
     * @param tokens
     */
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