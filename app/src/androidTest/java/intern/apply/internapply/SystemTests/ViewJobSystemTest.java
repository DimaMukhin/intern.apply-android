package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.mainactivity.MainActivity;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;

public class ViewJobSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public ViewJobSystemTest() {
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

    public void testFirstJobShowing(){
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickInList(1);
        solo.waitForActivity(ViewJobActivity.class);

        String[] job = {
          "fake org",
          "fake title",
          "fake location"
        };

        TestHelper.findStrings(job, solo);
    }


}
