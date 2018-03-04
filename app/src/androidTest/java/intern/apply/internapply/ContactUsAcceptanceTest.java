package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.ContactMessage;
import intern.apply.internapply.model.ServerError;
import intern.apply.internapply.view.contactusactivity.ContactUsActivity;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactUsAcceptanceTest extends ActivityInstrumentationTestCase2<ContactUsActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";

    private Solo solo;
    private final InternAPIProvider api;

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

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(1, "Invalid email address"));
        Observable<ContactMessage> output = Observable.error(CreateHttpException(errors));
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid email address"));
    }

    public void testInvalidTitleSendMessage() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(2, "Invalid title (max 100 characters)"));
        Observable<ContactMessage> output = Observable.error(CreateHttpException(errors));
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid title"));
    }

    public void testInvalidBodySendMessage() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, ContactUsActivity.class);
        solo.waitForActivity(ContactUsActivity.class);

        List<ServerError> errors = new ArrayList<>();
        errors.add(new ServerError(3, "Invalid message body (max 300 characters)"));
        Observable<ContactMessage> output = Observable.error(CreateHttpException(errors));
        when(api.sendContactMessage(any())).thenReturn(output);
        getActivity().setApi(api);

        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText("Invalid message body"));
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
