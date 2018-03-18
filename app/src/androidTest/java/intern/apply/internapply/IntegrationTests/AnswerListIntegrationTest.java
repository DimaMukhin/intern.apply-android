package intern.apply.internapply.IntegrationTests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Answer;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.view.viewquestionactivity.ViewQuestionActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnswerListIntegrationTest extends ActivityInstrumentationTestCase2<ViewQuestionActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPI api;
    private Solo solo;
    private String[] singleAnswerData;
    private String[] multipleAnswersData;
    private List<Answer> singleAnswer;
    private List<Answer> multipleAnswers;
    private String[] questionData;
    private Question question;

    public AnswerListIntegrationTest() {
        super(ViewQuestionActivity.class);
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
        testHelper(Observable.fromArray(singleAnswer), singleAnswerData);
    }

    public void testMultipleQuestionsShowing() {
        testHelper(Observable.fromArray(multipleAnswers), multipleAnswersData);
    }

    public void testEmptyQuestionListShowing() {
        testHelper(Observable.fromArray(new ArrayList<>()), new String[0]);
    }

    private void populateFakeQuestions() {
        singleAnswerData = new String[]{
                "dima", "test answer"
        };
        singleAnswer = new ArrayList<>();
        for (int i = 0; i < singleAnswerData.length; i += 2)
            singleAnswer.add(new Answer(singleAnswerData[i], singleAnswerData[i + 1]));

        multipleAnswersData = new String[]{
                "multi author", "multi dima",
                "another author", "answer",
                "look behind you", "a 3 headed monkey!"
        };
        multipleAnswers = new ArrayList<>();
        for (int i = 0; i < multipleAnswersData.length; i += 2)
            multipleAnswers.add(new Answer(multipleAnswersData[i], multipleAnswersData[i + 1]));

        questionData = new String[]{
                "fake question title",
                "fake question body",
                "fake author"
        };

        question = new Question(questionData[0], questionData[1], questionData[2]);
    }

    private void testHelper(Observable<List<Answer>> output, String[] data) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewQuestionActivity.class);
        solo.waitForActivity(ViewQuestionActivity.class);

        Observable<Question> outputQuestion = Observable.fromArray(question);
        when(api.getQuestion(anyInt())).thenReturn(outputQuestion);
        when(api.getAnswers(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.questionView);
        TestHelper.findStrings(data, solo);
    }
}
