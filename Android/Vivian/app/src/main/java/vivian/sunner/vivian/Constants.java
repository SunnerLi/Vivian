package vivian.sunner.vivian;

/**
 * Created by sunner on 2016/7/12.
 */
public class Constants {
    // Tag
    public final static String TAG = "---> 教師";

    // Type of procedure
    public final static int PRESS_BUTTON = 1;
    public final static int GIVE_QUESTION = 2;
    public final static int RECOVER_FIELD = 3;

    // Type of answer
    public final static int CORRECT = 1;
    public final static int WRONG = 0;

    // Spinner selection
    public static String[] questionType = {"English", "Chinese", "Mix"};

    // Broadcast filter
    public static String SETTING_FILTER = "setting filter";

    // Setting type
    public final static int OPEN_SPEAKING = 0;
    public final static int SHOW_Q_TEXT = 1;
    public final static int SHOW_ANSWER = 2;
    public final static int Q_TYPE = 3;

    // Setting Bundle type ( From setting 2 main )
    public final static String REVISE_INDEX = "revise index";
    public final static String REVISE_VALUE = "revise value";

    // Setting Bundle type ( From main 2 setting )
    public final static String OPEN_SPEAKING_BUNDLE_KEY = "open speaking bundle key";
    public final static String SHOW_Q_TEST_BUNDLE_KEY = "show quetion test bundle key";
    public final static String SHOW_ANSWER_BUNDLE_KEY = "show answer bundle key";
    public final static String Q_TYPE_BUNDLE_KEY = "question type bundle key";
}
