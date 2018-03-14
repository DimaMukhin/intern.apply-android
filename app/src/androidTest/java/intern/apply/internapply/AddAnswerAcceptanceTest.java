package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Answer;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.addansweractivity.AddAnswerActivity;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;

import io.reactivex.Observable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddAnswerAcceptanceTest extends ActivityInstrumentationTestCase2<AddAnswerActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPI api;

    public AddAnswerAcceptanceTest() {
        super(AddAnswerActivity.class);
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

    public void testValidAnswerAdded() {
        testHelper(Observable.just(new Answer("shai", "test answer")), "Your answer was successfully added");
    }

    public void testAddAnswerInternalServerError() {
        testHelper(Observable.error(new Error()), "Internal server error, please try again later");
    }

    public void testAddAnswerWithInvalidBody() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(34, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid answer");
    }

    public void testAddAnswerWithInvalidName() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(33, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid name");
    }

    private void testHelper(Observable<Answer> output, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddAnswerActivity.class);
        solo.waitForActivity(AddAnswerActivity.class);

        when(api.addAnswer(anyInt(), any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("answer");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToSearch));
    }
}
