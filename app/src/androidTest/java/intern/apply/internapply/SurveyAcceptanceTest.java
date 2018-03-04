package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.model.SurveyQuestion;
import intern.apply.internapply.view.surveyactivity.SurveyActivity;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyAcceptanceTest extends ActivityInstrumentationTestCase2<SurveyActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPIProvider api;

    private String[] questionsData;
    private List<String[]> responsesData;
    private List<SurveyQuestion> questionsList;

    public SurveyAcceptanceTest() {
        super(SurveyActivity.class);
        api = mock(InternAPIProvider.class);
        populateFakeQuestions();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent().putExtra("TEST", true));
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    private void populateFakeQuestions() {
        questionsData = new String[]{
                "Am I a test?",
                "To test or not to test?"
        };
        responsesData = new ArrayList<>(Arrays.asList(
                new String[]{"True", "False"},
                new String[]{"Wow", "I", "Test"}
        ));

        questionsList = new ArrayList<>();
        for (int i = 0; i < questionsData.length; i++)
            questionsList.add(new SurveyQuestion(questionsData[i], i + 1, Arrays.asList(responsesData.get(i))));
    }

    public void testSurveyShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        Observable<List<SurveyQuestion>> output = Observable.fromArray(questionsList);
        when(api.getSurvey()).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.SurveyListView);
        findStrings(questionsData);

        for (String[] responses : responsesData)
            findStrings(responses);
    }

    public void testValidSurveySent() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        Observable<List<SurveyQuestion>> questionOutput = Observable.fromArray(questionsList);
        Observable<CompletedSurvey> output = Observable.just(new CompletedSurvey(Arrays.asList("True", "Wow")));

        when(api.getSurvey()).thenReturn(questionOutput);
        when(api.sendCompletedSurvey(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Survey was sent successfully"));
    }

    public void testInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        Observable<List<SurveyQuestion>> questionOutput = Observable.fromArray(questionsList);
        Observable<CompletedSurvey> output = Observable.error(new Error());

        when(api.getSurvey()).thenReturn(questionOutput);
        when(api.sendCompletedSurvey(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testInvalidSurveySend() {
        String invalidSurveyMsg = "Please respond to every question";
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(51, invalidSurveyMsg));

        Observable<List<SurveyQuestion>> questionOutput = Observable.fromArray(questionsList);
        Observable<CompletedSurvey> output = Observable.error(CreateHttpException(errors));

        when(api.getSurvey()).thenReturn(questionOutput);
        when(api.sendCompletedSurvey(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(invalidSurveyMsg));
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

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND, solo.waitForText(s));
        }
    }
}
