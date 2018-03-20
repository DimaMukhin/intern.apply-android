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

    public void testFoundJobByTitle() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Software Developer", "IBM"
        };
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "SDE","Amazon"
        };
        testHelper(sql, searchedData,"Software Developer", true, nonExistent);
    }

    public void testFoundJobByOrganization() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
        };
        String[] nonExistent = new String[]{
                "SDE","Amazon",
                "Software Developer", "IBM"
        };
        testHelper(sql, searchedData,"Facebook", true, nonExistent);
    }

    public void testFoundJobByLocation() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "SDE","Amazon"
        };
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM"
        };
        testHelper(sql, searchedData,"Newyork", true, nonExistent);
    }

    public void testMultipleJobsShowing() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM"
        };
        String[] nonExistent = new String[]{
                "SDE","Amazon"
        };
        testHelper(sql, searchedData,"Software", true, nonExistent);
    }

    public void testTooLongQuery() {
        String longSearchQuery = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry";
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] searchedData = new String[]{
                "Search query has to be less than 100 characters"
        };

        testHelper(sql, searchedData,longSearchQuery, false, null);
    }
    public void testNoJobsFound() {
        String sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'Facebook', 'Software Engineering', 'fake location', 0, 0)," +
                "(2, 'Amazon', 'SDE', 'Newyork', 0, 0)," +
                "(3, 'IBM', 'Software Developer', 'Ottawa', 0, 0)";
        String[] nonExistent = new String[]{
                "Software Engineering", "Facebook",
                "Software Developer", "IBM",
                "SDE","Amazon"
        };

        testHelper(sql, new String[0],"Dropbox", true, nonExistent);
    }

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
        testHelper(sql, searchedData,"",false, null);
    }

    private void testHelper(String insertSqlStatements, String[] data, String searchQuery,Boolean nonExistentQuery, String[] nonExistent) {
        TestDBHelper.ExecuteSQL(insertSqlStatements);
        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.waitForView(R.id.JobsListView);

        solo.enterText((EditText) solo.getView(R.id.searchBox), searchQuery);
        TestHelper.findStrings(data, solo);
        if(nonExistentQuery){ TestHelper.nonExistentStrings(nonExistent, solo);}

    }
}