package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.By;
import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.contactusactivity.ContactUsActivity;
import intern.apply.internapply.view.mainactivity.MainActivity;

public class ContactUsSystemTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public ContactUsSystemTest() {
        super(MainActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
        TestDBHelper.createContactUsTable();
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
        TestDBHelper.CleanTables();
    }

    public void testMessageSentSuccessfully() {
        startTestState();

        solo.enterText((EditText) solo.getView(R.id.etEmail), "this@is.com");
        solo.enterText((EditText) solo.getView(R.id.etTitle), "valid title");
        solo.enterText((EditText) solo.getView(R.id.etMessage), "valid message");

        sendMessageTestState("Message was sent successfully");
    }

    public void testEmptyMessageNotSent() {
        startTestState();

        sendMessageTestState("Invalid email address");
    }

    public void testInvalidEmailMessageNotSent() {
        startTestState();

        solo.enterText((EditText) solo.getView(R.id.etEmail), "this");
        solo.enterText((EditText) solo.getView(R.id.etTitle), "valid title");
        solo.enterText((EditText) solo.getView(R.id.etMessage), "valid message");

        sendMessageTestState("Invalid email address");
    }

    public void testInvalidTitleMessageNotSent() {
        startTestState();

        solo.enterText((EditText) solo.getView(R.id.etEmail), "this@is.com");
        solo.enterText((EditText) solo.getView(R.id.etTitle), "");
        solo.enterText((EditText) solo.getView(R.id.etMessage), "valid message");

        sendMessageTestState("Invalid title");
    }

    public void testInvalidBodyMessageNotSent() {
        startTestState();

        solo.enterText((EditText) solo.getView(R.id.etEmail), "this@is.com");
        solo.enterText((EditText) solo.getView(R.id.etTitle), "valid title");
        solo.enterText((EditText) solo.getView(R.id.etMessage), "");

        sendMessageTestState("Invalid message body");
    }

    private void startTestState() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.clickOnMenuItem("Contact us");
        solo.waitForActivity(ContactUsActivity.class);
    }

    private void sendMessageTestState(String expectedText) {
        solo.clickOnButton("Send");
        assertTrue(TEXT_NOT_FOUND, solo.searchText(expectedText));
    }
}
