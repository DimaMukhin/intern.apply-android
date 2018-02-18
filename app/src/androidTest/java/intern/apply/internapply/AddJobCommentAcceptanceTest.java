package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.Job;
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

public class AddJobCommentAcceptanceTest extends ActivityInstrumentationTestCase2<ViewJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;

    public AddJobCommentAcceptanceTest() {
        super(ViewJobActivity.class);
        api = mock(InternAPI.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());

        Job fakeJob = new Job(
                "fake org",
                "fake title",
                "fake location",
                "fake description");
        List<Job> fakeJobList = new ArrayList<Job>();
        fakeJobList.add(fakeJob);
        Observable<List<Job>> output = Observable.just(fakeJobList);
        when(api.getJob(anyInt())).thenReturn(output);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidCommentAdded() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        Observable<Comment> output = Observable.just(new Comment());
        when(api.addJobComment(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Comment was sent successfully"));
    }

    public void testAddCommentInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        Observable<Comment> output = Observable.error(new Error());
        when(api.addJobComment(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testAddCommentWithInvalidName() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(6, ""));
        Observable<Comment> output = Observable.error(CreateHttpException(errors));
        when(api.addJobComment(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid name"));
    }

    public void testAddCommentWithInvalidBody() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewJobActivity.class);
        solo.waitForActivity(ViewJobActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(5, ""));
        Observable<Comment> output = Observable.error(CreateHttpException(errors));
        when(api.addJobComment(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid comment body"));
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
