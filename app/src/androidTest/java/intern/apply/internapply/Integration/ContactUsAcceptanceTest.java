package intern.apply.internapply.Integration;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.contactusactivity.ContactUsActivity;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactUsAcceptanceTest extends ActivityInstrumentationTestCase2<ContactUsActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private final InternAPIProvider api;
    private Solo solo;

    public ContactUsAcceptanceTest() {
        super(ContactUsActivity.class);
        api = mock(InternAPIProvider.class);
        setActivityIntent(new Intent().putExtra("TEST", true));
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

    public void testValidMessageSent() {
        testHelper(Observable.just(new ContactMessage()), "Message was sent successfully");
    }

    public void testInternalServerError() {
        testHelper(Observable.error(new Error()), "Internal server error, please try again later");
    }

    public void testInvalidEmailSendMessage() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(1, "Invalid email address"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid email address");
    }

    public void testInvalidTitleSendMessage() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(2, "Invalid title (max 100 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid title");
    }

    public void testInvalidBodySendMessage() {
        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(3, "Invalid message body (max 300 characters)"));
        testHelper(Observable.error(TestHelper.CreateHttpException(errors)), "Invalid message body");
    }

    private void testHelper(Observable<ContactMessage> output, String textToSearch) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(textToSearch));
    }
}