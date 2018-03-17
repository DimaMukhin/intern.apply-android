package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyIntegrationTest extends ActivityInstrumentationTestCase2<SurveyActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPIProvider api;
    private Solo solo;
    private String[] questionsData;
    private List<String[]> responsesData;
    private List<SurveyQuestion> questionsList;

    public SurveyIntegrationTest() {
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
        TestHelper.findStrings(questionsData, solo);

        for (String[] responses : responsesData)
            TestHelper.findStrings(responses, solo);
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
        Observable<CompletedSurvey> output = Observable.error(TestHelper.CreateHttpException(errors));

        when(api.getSurvey()).thenReturn(questionOutput);
        when(api.sendCompletedSurvey(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Submit");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(invalidSurveyMsg));
    }
}
