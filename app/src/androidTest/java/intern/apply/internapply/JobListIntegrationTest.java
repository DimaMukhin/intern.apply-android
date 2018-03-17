package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.view.mainactivity.MainActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobListIntegrationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private final InternAPIProvider api;
    private Solo solo;
    private String[] singleJobData;
    private String[] multipleJobsData;
    private List<Job> singleJob;
    private List<Job> multipleJobs;

    public JobListIntegrationTest() {
        super(MainActivity.class);
        api = mock(InternAPIProvider.class);
        setActivityIntent(new Intent().putExtra("TEST", true));
        PopulateFakeJobs();
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testOneJobShowing() {
        testHelper(Observable.fromArray(singleJob), singleJobData);
    }

    public void testMultipleJobsShowing() {
        testHelper(Observable.fromArray(multipleJobs), multipleJobsData);
    }

    public void testEmptyJobListShowing() {
        testHelper(Observable.fromArray(new ArrayList<Job>()), new String[0]);
    }

    private void testHelper(Observable<List<Job>> output, String[] data) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        when(api.getAllJobs()).thenReturn(output);
        getActivity().SetAPI(api);

        solo.waitForView(R.id.JobsListView);
        TestHelper.findStrings(data, solo);
    }

    private void PopulateFakeJobs() {
        singleJobData = new String[]{
                "Software Engineering", "Facebook"
        };
        singleJob = new ArrayList<>();
        for (int i = 0; i < singleJobData.length; i += 2)
            singleJob.add(new JobBuilder().setTitle(singleJobData[i]).setOrganization(singleJobData[i + 1]).createJob());

        multipleJobsData = new String[]{
                "T1", "C1",
                "Title 2", "Company 2",
                "Software Engineering", "Facebook"
        };
        multipleJobs = new ArrayList<>();
        for (int i = 0; i < multipleJobsData.length; i += 2)
            multipleJobs.add(new JobBuilder().setTitle(multipleJobsData[i]).setOrganization(multipleJobsData[i + 1]).createJob());
    }
}
