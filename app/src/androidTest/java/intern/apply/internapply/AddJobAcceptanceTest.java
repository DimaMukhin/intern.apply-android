package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.addjobactivity.AddJobActivity;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddJobAcceptanceTest extends ActivityInstrumentationTestCase2<AddJobActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;


    public AddJobAcceptanceTest() {
        super(AddJobActivity.class);
        api = mock(InternAPI.class);
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
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddJobActivity.class);
        solo.waitForActivity(AddJobActivity.class);

        Observable<Job> output = Observable.just(new Job(null, null));
        when(api.addJob(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Job added successfully"));
    }

    public void testInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddJobActivity.class);
        solo.waitForActivity(AddJobActivity.class);

        Observable<Job> output = Observable.error(new Error());
        when(api.addJob(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testInvalidJobOrganization() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddJobActivity.class);
        solo.waitForActivity(AddJobActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(11, "Invalid job organization (max 45 characters)"));
        Observable<Job> output = Observable.error(CreateHttpException(errors));
        when(api.addJob(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid job organization"));
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
