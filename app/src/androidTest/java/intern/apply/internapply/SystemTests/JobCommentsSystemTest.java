package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.jobcommentsactivity.JobCommentsActivity;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;

public class JobCommentsSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public JobCommentsSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.CreateJobTables();
        TestDBHelper.InitializeJobTables();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testFakeOrgCommentsShowing() {
        startTestState(0);

        String[] comments = {
                "this is a nice comment body", "dima",
                "another comment for the same job", "ben" };
        TestHelper.findStrings(comments, solo);
    }

    public void testNoCommentsForCityOfWinnipeg() throws Exception {
        startTestState(3);

        ListView lv = ((ListView) solo.getView(R.id.commentsListView));
        solo.waitForView(lv);
        if (lv.getAdapter() != null) {
            assertEquals(lv.getAdapter().getCount(), 0);
        }
    }

    private void startTestState(int listItem) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(listItem);
        solo.waitForActivity(ViewJobActivity.class);

        solo.clickOnButton("Comments");
        solo.waitForActivity(JobCommentsActivity.class);
    }
}
