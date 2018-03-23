package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.addjobactivity.AddJobActivity;
import intern.apply.internapply.view.mainactivity.MainActivity;

public class AddJobSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private EditText jobOrg, jobTitle, jobLoc, jobUrl, jobDesc;
    private Solo solo;

    public AddJobSystemTest() {
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

    public void testAddNewValidJob() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "New job organization");
        solo.enterText(jobTitle, "New job title");
        solo.enterText(jobLoc, "New job valid location");
        solo.enterText(jobUrl, "https://www.validUrl.com");
        solo.enterText(jobDesc, "New job valid description");

        submitAndCheckForMessage("Job added successfully");

        //check if the job was added by going to the home page
        solo.clickOnImageButton(0);
        String[] job = new String[]{"New job organization", "New job title"};
        TestHelper.findStrings(job, solo);
    }

    public void testInvalidJobOrganization() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "");
        solo.enterText(jobTitle, "valid title");
        solo.enterText(jobLoc, "valid location");
        solo.enterText(jobUrl, "https://www.validUrl.com");
        solo.enterText(jobDesc, "valid description");

        submitAndCheckForMessage("Invalid job organization");

        solo.clickOnImageButton(0);
        solo.waitForActivity(MainActivity.class);

        String[] job = {"CityOFWinnipeg", "third title"};
        TestHelper.findStrings(job, solo);
    }

    public void testInvalidJobTitle() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "valid organization");
        solo.enterText(jobTitle, "");
        solo.enterText(jobLoc, "valid location");
        solo.enterText(jobUrl, "https://www.validUrl.com");
        solo.enterText(jobDesc, "valid description");

        submitAndCheckForMessage("Invalid job title");

        solo.clickOnImageButton(0);
        solo.waitForActivity(MainActivity.class);

        String[] job = {"CityOFWinnipeg", "third title"};
        TestHelper.findStrings(job, solo);
    }

    public void testInvalidJobLocation() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "valid organization");
        solo.enterText(jobTitle, "valid title");
        solo.enterText(jobLoc, "");
        solo.enterText(jobUrl, "https://www.validUrl.com");
        solo.enterText(jobDesc, "valid description");

        submitAndCheckForMessage("Invalid job location");

        solo.clickOnImageButton(0);
        solo.waitForActivity(MainActivity.class);

        String[] job = {"CityOFWinnipeg", "third title"};
        TestHelper.findStrings(job, solo);
    }

    public void testInvalidJobUrl() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "valid organization");
        solo.enterText(jobTitle, "valid title");
        solo.enterText(jobLoc, "valid location");
        solo.enterText(jobUrl, "");
        solo.enterText(jobDesc, "valid description");

        submitAndCheckForMessage("Invalid job URL");

        //Still invalid since URL doesn't start with http or https
        solo.enterText(jobUrl, "www.invalidUrl.com");

        submitAndCheckForMessage("Invalid job URL");

        solo.clickOnImageButton(0);
        solo.waitForActivity(MainActivity.class);

        String[] job = {"CityOFWinnipeg", "third title"};
        TestHelper.findStrings(job, solo);
    }

    public void testInvalidJobDescription() {
        navigateToAddJob();
        setFormFields();

        solo.enterText(jobOrg, "valid organization");
        solo.enterText(jobTitle, "valid title");
        solo.enterText(jobLoc, "valid location");
        solo.enterText(jobUrl, "https://www.validUrl.com");
        solo.enterText(jobDesc, "");

        submitAndCheckForMessage("Invalid job description");

        solo.clickOnImageButton(0);
        solo.waitForActivity(MainActivity.class);

        String[] job = {"CityOFWinnipeg", "third title"};
        TestHelper.findStrings(job, solo);
    }

    private void setFormFields() {
        jobOrg = (EditText) solo.getView(R.id.etJobOrg);
        jobTitle = (EditText) solo.getView(R.id.etJobTitle);
        jobLoc = (EditText) solo.getView(R.id.etJobLoc);
        jobUrl = (EditText) solo.getView(R.id.etJobUrl);
        jobDesc = (EditText) solo.getView(R.id.etJobDesc);
    }

    private void submitAndCheckForMessage(String msg) {
        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(msg));
    }

    private void navigateToAddJob() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText("Add Job");
        solo.waitForActivity(AddJobActivity.class);
    }
}
