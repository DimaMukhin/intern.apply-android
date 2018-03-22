package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;

public class JobRatingSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public JobRatingSystemTest() {
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

    public void testFirstJobRatingShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(1);
        solo.waitForActivity(ViewJobActivity.class);

        String[] jobRating = {"1", "votes"};

        TestHelper.findStrings(jobRating, solo);
    }

    public void testThirdJobVote() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(3);
        solo.waitForActivity(ViewJobActivity.class);

        solo.clickOnView(solo.getView(R.id.ratingBar));

        String[] jobRating = {"1", "votes"};

        TestHelper.findStrings(jobRating, solo);
    }
}