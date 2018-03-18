package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddQuestionIntegrationTest extends ActivityInstrumentationTestCase2<AddQuestionActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPI api;
    private Solo solo;

    public AddQuestionIntegrationTest() {
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
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testValidQuestionAdded() {
        testHelper(Observable.just(new Question()), "Question was added successfully");
    }

    public void testAddQuestionInternalServerError() {
        testHelper(Observable.error(new Error()), "Internal server error, please try again later");
    }

    public void testAddQuestionWithInvalidTitle() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(7, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid question title");
    }

    public void testAddQuestionWithInvalidBody() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(8, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid question body");
    }

    public void testAddQuestionWithInvalidName() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(9, ""));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid name");
    }

    private void testHelper(Observable<Question> output, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, AddQuestionActivity.class);
        solo.waitForActivity(AddQuestionActivity.class);

        when(api.addNewQuestion(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Ask");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToSearch));
    }
}
