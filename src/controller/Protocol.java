package controller;

/**
 * Created by Frank on 20-12-16.
 */
public interface Protocol {

    String FILE_LOCATION = "/Users/Frank/Desktop/blogs/";
    String TEST_LOCATION = "/Users/Frank/Desktop/test/";

    enum classes {
        M,
        F
    }

    int SMOOTHING_K = 1;
    int FEATURE_THRESHOLD = 5;
}
