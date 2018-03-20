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
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testFoundSearchResult() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM"
        };
        testHelper(sql, searchedData,"Software");
    }

    /*public void testMultipleJobsShowing() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'C1', 'T1', 'fake location', 0, 0)," +
                "(2, 'Company 2', 'Title 2', 'fake location', 0, 0)," +
                "(3, 'Facebook', 'Software Engineering', 'fake location', 0, 0)";
        String[] multipleJobsData = new String[]{
                "T1", "C1",
                "Title 2", "Company 2",
                "Software Engineering", "Facebook"
        };
        testHelper(sql, multipleJobsData);
    }*/

    public void testEmptySearch() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM",
                "SDE", "Amazon"
        };
        testHelper(sql, searchedData,"");
    }

    private void testHelper(String insertSqlStatements, String[] data, String searchQuery) {
        TestDBHelper.ExecuteSQL(insertSqlStatements);
        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.waitForView(R.id.JobsListView);

        solo.enterText((EditText) solo.getView(R.id.searchBox), searchQuery);
        TestHelper.findStrings(data, solo);

    }
}