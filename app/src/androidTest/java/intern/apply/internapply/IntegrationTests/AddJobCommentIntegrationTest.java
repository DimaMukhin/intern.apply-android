package intern.apply.internapply.IntegrationTests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.viewjobactivity.ViewJobActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddJobCommentIntegrationTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPIProvider api;
    private Solo solo;

    public AddJobCommentIntegrationTest() {
        super(ViewJobActivity.class);
        api = mock(InternAPIProvider.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());

        Job fakeJob = new JobBuilder().setOrganization("fake org").setTitle("fake title").setLocation("fake location").setDescription("fake description").createJob();
        List<Job> fakeJobList = new ArrayList<>();
        fakeJobList.add(fakeJob);
        when(api.getJob(anyInt())).thenReturn(Observable.just(fakeJobList));
        when(api.getJobRating(anyInt())).thenReturn(Observable.fromArray());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidCommentAdded() {
        testHelper(Observable.just(new Comment()), "Comment was sent successfully");
    }

    public void testAddCommentInternalServerError() {
        testHelper(Observable.error(new Error()), "Internal server error, please try again later");
    }

    public void testAddCommentWithInvalidName() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(6, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid name");
    }

    public void testAddCommentWithInvalidBody() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(5, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid comment body");
    }

    private void testHelper(Observable<Comment> output, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        when(api.addJobComment(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToSearch));
    }
}
