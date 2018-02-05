package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import net.bytebuddy.implementation.bytecode.Throw;

import java.util.LinkedList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.model.ServerErrorException;
import intern.apply.internapply.view.contactusactivity.ContactUsActivity;
import io.reactivex.Observable;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactUsAcceptanceTest extends ActivityInstrumentationTestCase2<ContactUsActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private InternAPI api;

    public ContactUsAcceptanceTest() {
        super(ContactUsActivity.class);
        api = mock(InternAPI.class);
        setActivityIntent(new Intent().putExtra("TEST", true));
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testValidMessageSent() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        Observable<ContactMessage> output = Observable.just(new ContactMessage());
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Message was sent successfully"));
    }

    public void testInternalServerError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        Observable<ContactMessage> output = Observable.error(new Error());
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Internal server error, please try again later"));
    }

    public void testInvalidEmailSendMessage() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        ServerErrorException errs = new ServerErrorException();
        errs.addErrorCode(1);
        Observable<ContactMessage> output = Observable.error(errs);
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid email address"));
    }

    public void testInvalidTitleSendMessage() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        ServerErrorException errs = new ServerErrorException();
        errs.addErrorCode(2);
        Observable<ContactMessage> output = Observable.error(errs);
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid title"));
    }

    public void testInvalidBodySendMessage() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        ServerErrorException errs = new ServerErrorException();
        errs.addErrorCode(3);
        Observable<ContactMessage> output = Observable.error(errs);
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid message body"));
    }
}
