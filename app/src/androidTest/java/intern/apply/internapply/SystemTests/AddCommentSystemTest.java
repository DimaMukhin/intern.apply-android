package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.jobcommentsactivity.JobCommentsActivity;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;

public class AddCommentSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public AddCommentSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
        TestDBHelper.CreateJobTables();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.InitializeJobTables();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testAddNewValidComment() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(2);
        solo.waitForActivity(ViewJobActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etName), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etMessageComment), "hello I am robotium");

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Comment was sent successfully"));

        solo.clickOnButton("Comments");
        solo.waitForActivity(JobCommentsActivity.class);

        String[] comments = {
                "hello I am robotium", "robotium",
                "this last comment is for job 2", "rick" };
        TestHelper.findStrings(comments, solo);
    }

    public void testCommentInvalidNameNotAdded() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(2);
        solo.waitForActivity(ViewJobActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etName), "");
        solo.enterText((EditText) solo.getView(R.id.etMessageComment), "hello I am robotium");

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid name"));

        solo.clickOnButton("Comments");
        solo.waitForActivity(JobCommentsActivity.class);

        String[] comments = { "this last comment is for job 2", "rick" };
        TestHelper.findStrings(comments, solo);
    }

    public void testCommentInvalidBodyNotAdded() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(2);
        solo.waitForActivity(ViewJobActivity.class);

        solo.enterText((EditText) solo.getView(R.id.etName), "robotium");
        solo.enterText((EditText) solo.getView(R.id.etMessageComment), "");

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid comment body"));

        solo.clickOnButton("Comments");
        solo.waitForActivity(JobCommentsActivity.class);

        String[] comments = { "this last comment is for job 2", "rick" };
        TestHelper.findStrings(comments, solo);
    }
}
