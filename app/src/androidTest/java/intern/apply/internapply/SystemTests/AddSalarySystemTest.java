package intern.apply.internapply.SystemTests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;


public class AddSalarySystemTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public AddSalarySystemTest() {
        super(ViewJobActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
    }

    private void setupTables() {

        String sql = "DROP TABLE IF EXISTS jobRating";
        TestHelper.ExecuteSQL(sql);

        sql = "DROP TABLE IF EXISTS job;";
        TestHelper.ExecuteSQL(sql);

        sql = " CREATE TABLE job (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "organization VARCHAR(45) NOT NULL," +
                "title VARCHAR(100) NOT NULL," +
                "location VARCHAR(45)," +
                "description VARCHAR(2000)," +
                "salary DECIMAL(4,1)," +
                "numSalaries INT(10)," +
                "PRIMARY KEY (id))";
        TestHelper.ExecuteSQL(sql);


        sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'fake org', 'fake title', 'fake location', 0, 0)," +
                "(2, 'google', 'second title', 'vancouver', 0, 0)," +
                "(3, 'CityOFWinnipeg', 'third title', 'location', 0, 0)";
        TestHelper.ExecuteSQL(sql);


        sql = "CREATE TABLE jobRating (" +
                "jobId INT(11) NOT NULL," +
                "score DECIMAL(3,2) DEFAULT '0.00' NOT NULL," +
                "votes INT(11) DEFAULT '0' NOT NULL," +
                "PRIMARY KEY(jobId)," +
                "CONSTRAINT jobId___fk FOREIGN KEY (jobId) REFERENCES job (id) ON DELETE CASCADE )";
        TestHelper.ExecuteSQL(sql);

        sql = "INSERT INTO jobRating(jobId, score, votes) VALUES" +
                "(1, 1.0, 1)," +
                "(2, 2.0, 2)";
        TestHelper.ExecuteSQL(sql);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupTables();
        setActivityIntent(new Intent().putExtra("jobId", 2));
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidSalaryAdded() {
        testHelper("5000", 1, solo.getString(R.string.SalarySuccess));
        assertTrue(TEXT_NOT_FOUND, solo.waitForText("Average Salary: 5.0k per year (1"));
    }


    public void testAddSalaryWithInvalidSalary() {
        testHelper("-5000", 1, "Invalid salary");
    }

    public void testAddSalaryWithInvalidSalaryType() {
        testHelper("5000", 0, "Please pick a pay duration");
    }

    private void testHelper(String salary, int salaryTypeDropdown, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText(salary);
        solo.pressSpinnerItem(0, salaryTypeDropdown);

        solo.clickOnButton("Submit");

        assertTrue(TEXT_NOT_FOUND, solo.waitForText(textToSearch));
    }
}