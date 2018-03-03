package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.view.viewquestionactivity.ViewQuestionActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Unknown on 2018-03-03.
 */

public class ViewQuestionAcceptanceTest extends ActivityInstrumentationTestCase2<ViewQuestionActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;
    private String[] questionData;
    private Question question;

    public ViewQuestionAcceptanceTest() {
        super(ViewQuestionActivity.class);
        api = mock(InternAPI.class);
        createFakeQuestion();
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

    public void testQuestionShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ViewQuestionActivity.class);
        solo.waitForActivity(ViewQuestionActivity.class);

        Observable<Question> output = Observable.fromArray(question);
        when(api.getQuestion(anyInt())).thenReturn(output);
        getActivity().setApi(api);

        solo.waitForView(R.id.questionView);
        findStrings(questionData);
    }

    private void createFakeQuestion() {
        questionData = new String[] {
                "fake question title",
                "fake question body",
                "fake author"
        };

        question = new Question(questionData[0], questionData[1], questionData[2]);
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND, solo.waitForText(s));
        }
    }
}
