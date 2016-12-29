package controller;

/**
 * Created by Frank on 20-12-16.
 */
public interface Protocol {

    String FILE_LOCATION = "../blogs/training/";
    String TEST_LOCATION = "../blogs/test/";

    enum classes {
        MALE,
        FEMALE
    }
    int SMOOTHING_K = 1;
    int FEATURE_THRESHOLD = 5;


}
