package intern.apply.internapply.SystemTests;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import intern.apply.internapply.R;
import intern.apply.internapply.TestHelper;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.view.surveyactivity.SurveyActivity;

public class SurveySystemTest extends ActivityInstrumentationTestCase2<SurveyActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    public SurveySystemTest() {
        super(SurveyActivity.class);
        InternAPI.setBaseUrl(TestHelper.LOCAL_HOST_URL);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestDBHelper.createSurveyTables();
        TestDBHelper.initializeSurveyTables();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        TestDBHelper.CleanTables();
    }

    public void testSurveyShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        String[] surveyData = {
                "Is this a test?",
                "True", "False",
                "Test2?",
                "Disagree", "No Opinion", "Agree"};
        TestHelper.findStrings(surveyData, solo);

        assertFalse(solo.isRadioButtonChecked(0));
        assertFalse(solo.isRadioButtonChecked(1));
        assertFalse(solo.isRadioButtonChecked(2));
        assertFalse(solo.isRadioButtonChecked(3));
        assertFalse(solo.isRadioButtonChecked(4));
    }

    public void testSurveySubmit() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, SurveyActivity.class);
        solo.waitForActivity(SurveyActivity.class);

        solo.clickOnRadioButton(1);
        solo.clickOnRadioButton(3);

        assertTrue(solo.isRadioButtonChecked("False"));
        assertTrue(solo.isRadioButtonChecked("No Opinion"));

        solo.clickOnView(solo.getView(R.id.btSubmit));

        assertTrue(TEXT_NOT_FOUND, solo.searchText("Survey was sent successfully"));
    }
}
