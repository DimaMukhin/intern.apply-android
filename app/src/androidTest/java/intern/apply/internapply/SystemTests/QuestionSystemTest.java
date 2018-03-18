package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.qnaactivity.QNAActivity;
import intern.apply.internapply.view.viewquestionactivity.ViewQuestionActivity;

public class QuestionSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public QuestionSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
        TestDBHelper.createQNATables();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.initializeQNATables();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testAllQuestionsDisplayed() {
        navigateToQuestionList();

        String[] comments = {
                "first test title", "Dima",
                "how much time to find a job?", "Ben",
                "what are you looking at?", "dima" };
        TestHelper.findStrings(comments, solo);
    }

    public void testDisplayQuestionDetails() {
        navigateToQuestionList();

        solo.clickInList(1);
        solo.waitForActivity(ViewQuestionActivity.class);

        String[] comments = { "first test title", "Dima", "this is the body" };
        TestHelper.findStrings(comments, solo);
    }

    public void testAddValidQuestion() {
        navigateToQuestionList();

        solo.clickOnView(solo.getView(R.id.floatingAddQuestion));
        solo.waitForActivity(AddQuestionActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etQuestionName), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etQuestionTitle), "robo title");
        solo.enterText((EditText) solo.getView(R.id.etQuestionBody), "robo body");

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Question was added successfully"));

        solo.waitForActivity(QNAActivity.class);
        String[] comments = {
                "first test title", "Dima",
                "how much time to find a job?", "Ben",
                "what are you looking at?", "dima",
                "robo title", "robotium" };
        TestHelper.findStrings(comments, solo);
    }

    public void testInvalidNameQuestionNotAdded() {
        navigateToQuestionList();

        solo.clickOnView(solo.getView(R.id.floatingAddQuestion));
        solo.waitForActivity(AddQuestionActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etQuestionName), "");
        solo.enterText((EditText) solo.getView(R.id.etQuestionTitle), "robo title");
        solo.enterText((EditText) solo.getView(R.id.etQuestionBody), "robo body");

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid name"));

        solo.goBack();
        solo.waitForActivity(QNAActivity.class);
        String[] comments = {
                "first test title", "Dima",
                "how much time to find a job?", "Ben",
                "what are you looking at?", "dima" };
        TestHelper.findStrings(comments, solo);
    }

    public void testInvalidTitleQuestionNotAdded() {
        navigateToQuestionList();

        solo.clickOnView(solo.getView(R.id.floatingAddQuestion));
        solo.waitForActivity(AddQuestionActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etQuestionName), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etQuestionTitle), "");
        solo.enterText((EditText) solo.getView(R.id.etQuestionBody), "robo body");

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid question title"));

        solo.goBack();
        solo.waitForActivity(QNAActivity.class);
        String[] comments = {
                "first test title", "Dima",
                "how much time to find a job?", "Ben",
                "what are you looking at?", "dima" };
        TestHelper.findStrings(comments, solo);
    }

    public void testInvalidBodyQuestionNotAdded() {
        navigateToQuestionList();

        solo.clickOnView(solo.getView(R.id.floatingAddQuestion));
        solo.waitForActivity(AddQuestionActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etQuestionName), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etQuestionTitle), "robo title");
        solo.enterText((EditText) solo.getView(R.id.etQuestionBody), "");

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid question body"));

        solo.goBack();
        solo.waitForActivity(QNAActivity.class);
        String[] comments = {
                "first test title", "Dima",
                "how much time to find a job?", "Ben",
                "what are you looking at?", "dima" };
        TestHelper.findStrings(comments, solo);
    }

    private void navigateToQuestionList() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText("Q&A");
        solo.waitForActivity(QNAActivity.class);
    }
}
