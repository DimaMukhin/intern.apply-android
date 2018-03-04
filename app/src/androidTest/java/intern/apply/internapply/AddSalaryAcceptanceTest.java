package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.model.Salary;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddSalaryAcceptanceTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPIProvider api;

    public AddSalaryAcceptanceTest() {
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
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidSalaryAdded() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        Observable<Salary> output = Observable.just(new Salary(2, "5", 1));
        when(api.addJobSalary(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText("5000");
        solo.pressSpinnerItem(0, 1);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(solo.getString(R.string.SalarySuccess)));
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Average Salary: 5k per year (1"));
    }

    public void testAddSalaryWithInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        Observable<Salary> output = Observable.error(new Error());
        when(api.addJobSalary(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText("5000");
        solo.pressSpinnerItem(0, 1);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testAddSalaryWithInvalidSalary() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(41, ""));
        Observable<Salary> output = Observable.error(CreateHttpException(errors));
        when(api.addJobSalary(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText("-5000");
        solo.pressSpinnerItem(0, 1);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid salary"));
    }

    public void testAddSalaryWithInvalidSalaryType() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(42, ""));
        Observable<Salary> output = Observable.error(CreateHttpException(errors));
        when(api.addJobSalary(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnView(solo.getView(R.id.salaryButton));

        EditText salaryInput = (EditText) (solo.getView(R.id.salaryInput));
        salaryInput.setText("5000");
        solo.pressSpinnerItem(0, 0);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Please pick a pay duration"));
    }

    private HttpException CreateHttpException(List<ServerError> errors) {
        JsonArray errorBody = new JsonArray();

        for (ServerError error : errors) {
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty("code", error.getCode());
            jsonError.addProperty("message", error.getMessage());
            errorBody.add(jsonError);
        }

        return new HttpException(
                Response.error(400,
                        ResponseBody.create(
                                MediaType.parse("application/json; charset=utf-8"),
                                errorBody.toString())));
    }
}