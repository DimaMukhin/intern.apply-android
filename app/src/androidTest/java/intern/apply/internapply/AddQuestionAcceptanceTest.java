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
import intern.apply.internapply.model.Question;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddQuestionAcceptanceTest extends ActivityInstrumentationTestCase2<AddQuestionActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;

    public AddQuestionAcceptanceTest() {
        super(AddQuestionActivity.class);
        api = mock(InternAPI.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws  Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidQuestionAdded() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        Observable<Question> output = Observable.just(new Question());
        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Question was added successfully"));
    }

    public void testAddQuestionInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        Observable<Question> output = Observable.error(new Error());
        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testAddQuestionWithInvalidTitle() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(7, ""));
        Observable<Question> output = Observable.error(CreateHttpException(errors));
        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid question title"));
    }

    public void testAddQuestionWithInvalidBody() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(8, ""));
        Observable<Question> output = Observable.error(CreateHttpException(errors));
        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid question body"));
    }

    public void testAddQuestionWithInvalidName() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(9, ""));
        Observable<Question> output = Observable.error(CreateHttpException(errors));
        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid name"));
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
