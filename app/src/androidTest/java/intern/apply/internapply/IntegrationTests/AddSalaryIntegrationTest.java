package intern.apply.internapply.IntegrationTests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.model.Salary;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddSalaryIntegrationTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPIProvider api;
    private Solo solo;

    public AddSalaryIntegrationTest() {
        super(ViewJobActivity.class);
        api = mock(InternAPIProvider.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());

        Job fakeJob = new JobBuilder().setOrganization("fake org").setTitle("fake title").setLocation("fake location").setDescription("fake description").setSalary(1).setNumSalaries(1).createJob();
        List<Job> fakeJobList = new ArrayList<>();
        fakeJobList.add(fakeJob);
        Observable<List<Job>> output = Observable.just(fakeJobList);
        when(api.getJob(anyInt())).thenReturn(output);
        when(api.getJobRating(anyInt())).thenReturn(Observable.fromArray());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidSalaryAdded() {
        testHelper(Observable.just(new Salary(2, "5", 1)), "5000", 1,
                solo.getString(R.string.SalarySuccess));
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Average Salary: 5k per year (1"));
    }

    public void testAddSalaryWithInternalServerError() {
        testHelper(Observable.error(new Error()), "5000", 1,
                "Internal server error, please try again later");
    }


    public void testAddSalaryWithInvalidSalary() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(41, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "-5000", 1,
                "Invalid salary");
    }

    public void testAddSalaryWithInvalidSalaryType() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(42, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "5000", 0,
                "Please pick a pay duration");
    }

    private void testHelper(Observable<Salary> output, String salary, int salaryTypeDropdown, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        when(api.addJobSalary(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText(salary);
        solo.pressSpinnerItem(0, salaryTypeDropdown);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToSearch));
    }
}