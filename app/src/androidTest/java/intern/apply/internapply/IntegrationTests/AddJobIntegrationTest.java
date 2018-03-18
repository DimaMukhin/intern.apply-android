package intern.apply.internapply.IntegrationTests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.addjobactivity.AddJobActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddJobIntegrationTest extends ActivityInstrumentationTestCase2<AddJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPIProvider api;
    private Solo solo;


    public AddJobIntegrationTest() {
        super(AddJobActivity.class);
        api = mock(InternAPIProvider.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidJobAdded() {
        testHelper(Observable.just(new JobBuilder().setTitle(null).setOrganization(null).createJob()), "Job added successfully");
    }

    public void testInternalServerError() {
        testHelper(Observable.error(new Error()), "Internal server error, please try again later");
    }

    public void testInvalidJobOrganization() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(11, "Invalid job organization (max 45 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid job organization");
    }

    public void testInvalidJobTitle() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(12, "Invalid job title (max 100 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid job title");
    }

    public void testInvalidJobLocation() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(13, "Invalid job location (max 45 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid job location");
    }

    public void testInvalidJobDescription() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(14, "Invalid job description (max 2000 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid job description");
    }

    public void testInvalidJobUrl() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(15, "Invalid job URL (max 1000 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid job URL");
    }

    private void testHelper(Observable<Job> output, String textToTest) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddJobActivity.class);
        solo.waitForActivity(AddJobActivity.class);

        when(api.addJob(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToTest));
    }
}
