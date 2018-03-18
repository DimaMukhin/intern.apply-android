package intern.apply.internapply.SystemTests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;


public class AddSalarySystemTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public AddSalarySystemTest() {
        super(ViewJobActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
        TestDBHelper.CreateJobTables();
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.InitializeJobTables();
        setActivityIntent(new Intent().putExtra("jobId", 2));
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
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