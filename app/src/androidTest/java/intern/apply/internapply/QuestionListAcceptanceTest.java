package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.view.qnaactivity.QNAActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionListAcceptanceTest extends ActivityInstrumentationTestCase2<QNAActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;

    private String[] singleQuestionData;
    private String[] multipleQuestionsData;
    private List<Question> singleQuestion;
    private List<Question> multipleQuestions;

    public QuestionListAcceptanceTest() {
        super(QNAActivity.class);
        api = mock(InternAPI.class);
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

    public void testOneQuestionShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, QNAActivity.class);
        solo.waitForActivity(QNAActivity.class);

        Observable<List<Question>> output = Observable.fromArray(singleQuestion);
        when(api.getAllQuestions()).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.questionsListView);
        findStrings(singleQuestionData);
    }

    public void testMultipleQuestionsShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, QNAActivity.class);
        solo.waitForActivity(QNAActivity.class);

        Observable<List<Question>> output = Observable.fromArray(multipleQuestions);
        when(api.getAllQuestions()).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.questionsListView);
        findStrings(multipleQuestionsData);
    }

    public void testEmptyQuestionListShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, QNAActivity.class);
        solo.waitForActivity(QNAActivity.class);

        Observable<List<Question>> output = Observable.fromArray(new ArrayList<Question>());
        when(api.getAllQuestions()).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.questionsListView);
    }

    private void populateFakeQuestions() {
        singleQuestionData = new String[] {
                "test title", "dima"
        };
        singleQuestion = new ArrayList<>();
        for (int i = 0; i < singleQuestionData.length; i += 2)
            singleQuestion.add(new Question(singleQuestionData[i], singleQuestionData[i+1]));

        multipleQuestionsData = new String[]{
                "multi title", "multi dima",
                "another title", "ben",
                "look behind you", "a 3 headed monkey!"
        };
        multipleQuestions = new ArrayList<>();
        for (int i = 0; i < multipleQuestionsData.length; i += 2)
            multipleQuestions.add(new Question(multipleQuestionsData[i], multipleQuestionsData[i+1]));
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND, solo.waitForText(s));
        }
    }
}
