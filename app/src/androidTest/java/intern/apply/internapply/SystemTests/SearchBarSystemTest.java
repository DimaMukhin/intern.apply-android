package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.mainactivity.MainActivity;


public class SearchBarSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private Solo solo;

    public SearchBarSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
    }

    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.CreateJobTables();
        TestDBHelper.initializeSearchJobTables();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testFoundJobByTitle() {

        String[] searchedData = new String[]{
                "Software Developer", "IBM"
        };
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "SDE","Amazon"
        };
        testHelper(searchedData, "Software Developer", true, nonExistent);
    }

    public void testFoundJobByOrganization() {

        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
        };
        String[] nonExistent = new String[]{
                "SDE","Amazon",
                "Software Developer", "IBM"
        };
        testHelper(searchedData, "Facebook", true, nonExistent);
    }

    public void testFoundJobByLocation() {

        String[] searchedData = new String[]{
                "SDE","Amazon"
        };
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM"
        };
        testHelper(searchedData, "Newyork", true, nonExistent);
    }

    public void testMultipleJobsShowing() {

        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM"
        };
        String[] nonExistent = new String[]{
                "SDE","Amazon"
        };
        testHelper(searchedData, "Software", true, nonExistent);
    }

    public void testTooLongQuery() {
        String longSearchQuery = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry";

        String[] searchedData = new String[]{
                "Search query has to be less than 100 characters"
        };

        testHelper(searchedData, longSearchQuery, false, null);
    }
    public void testNoJobsFound() {
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM",
                "SDE","Amazon"
        };

        testHelper(new String[0], "Dropbox", true, nonExistent);
    }

    public void testEmptySearch() {

        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM",
                "SDE", "Amazon"
        };
        testHelper(searchedData, "", false, null);
    }

    private void testHelper(String[] data, String searchQuery, Boolean nonExistentQuery, String[] nonExistent) {

        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.waitForView(R.id.JobsListView);

        solo.enterText((EditText) solo.getView(R.id.searchBox), searchQuery);
        TestHelper.findStrings(data, solo);
        if(nonExistentQuery){ TestHelper.nonExistentStrings(nonExistent, solo);}

    }
}