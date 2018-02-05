package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.view.mainactivity.MainActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobListAcceptanceTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    private InternAPI api;

    private String[] singleJobData;
    private String[] multipleJobsData;
    private List<Job> singleJob;
    private List<Job> multipleJobs;

    public JobListAcceptanceTest() {
        super(MainActivity.class);
        api = mock(InternAPI.class);
        setActivityIntent(new Intent().putExtra("TEST", true));
        PopulateFakeJobs();
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND, solo.waitForText(s));
        }
    }

    public void testOneJobShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(singleJob);
        when(api.getAllJobs()).thenReturn(output);
        getActivity().SetAPI(api);

        solo.waitForView(R.id.JobsListView);
        findStrings(singleJobData);
    }

    public void testMultipleJobsShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(multipleJobs);
        when(api.getAllJobs()).thenReturn(output);
        getActivity().SetAPI(api);

        solo.waitForView(R.id.JobsListView);
        findStrings(multipleJobsData);
    }

    public void testEmptyJobListShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(new ArrayList<Job>());
        when(api.getAllJobs()).thenReturn(output);
        getActivity().SetAPI(api);

        solo.waitForView(R.id.JobsListView);
    }

    public void PopulateFakeJobs() {
        singleJobData = new String[]{
                "Software Engineering", "Facebook"
        };
        singleJob = new ArrayList<>();
        for (int i = 0; i < singleJobData.length; i += 2)
            singleJob.add(new Job(singleJobData[i], singleJobData[i + 1]));

        multipleJobsData = new String[]{
                "T1", "C1",
                "Title 2", "Company 2",
                "Software Engineering", "Facebook"
        };
        multipleJobs = new ArrayList<>();
        for (int i = 0; i < multipleJobsData.length; i += 2)
            multipleJobs.add(new Job(multipleJobsData[i], multipleJobsData[i + 1]));
    }
}