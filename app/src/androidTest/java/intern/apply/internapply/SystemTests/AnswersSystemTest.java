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


public class AnswersSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public AnswersSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.createQNATables();
        TestDBHelper.initializeQNATables();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testAllAnswersDisplayed() {
        navigateToQuestionList();

        solo.clickInList(1);
        solo.waitForActivity(ViewQuestionActivity.class);

        String[] comments = {
                "Dima", "body of answer 1",
                "Dima", "body of answer 2"
        };
        TestHelper.findStrings(comments, solo);
    }

    public void testAddValidQuestion() {
        navigateToQuestionList();

        solo.clickInList(1);
        solo.waitForActivity(ViewQuestionActivity.class);

        solo.clickOnView(solo.getView(R.id.floatingAddAnswer));
        solo.waitForActivity(AddQuestionActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etAnswerAuthor), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etAnswerBody), "robo body");

        solo.clickOnButton("answer");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Your answer was successfully added"));

        solo.waitForActivity(ViewQuestionActivity.class);
        String[] answers = {
                "Dima", "body of answer 1",
                "Dima", "body of answer 2",
                "robotium", "robo body" };
        TestHelper.findStrings(answers, solo);
    }

    private void navigateToQuestionList() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText("Q&A");
        solo.waitForActivity(QNAActivity.class);
    }
}
