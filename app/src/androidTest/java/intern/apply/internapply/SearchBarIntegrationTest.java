package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.view.mainactivity.MainActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchBarIntegrationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;

    private InternAPIProvider api;
    private String[] multipleJobsData;
    private String[] emptyJobsData;
    private String[] allJobsData;

    private List<Job> multipleJobs;
    private List<Job> allJob;
    private List<Job> emptyJob;

    public SearchBarIntegrationTest() {
        super(MainActivity.class);
        api = mock(InternAPIProvider.class);
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

    public void testEmptySearch() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        when(api.getAllJobs()).thenReturn(Observable.fromArray(), Observable.fromArray(allJob));

        getActivity().SetAPI(api);
        solo.waitForView(R.id.JobsListView);

        solo.enterText((EditText) solo.getView(R.id.searchBox), "");
        TestHelper.findStrings(allJobsData, solo);

        verify(api, times(2)).getAllJobs();
    }

    public void testFoundSearchResult() {
        testHelper("Soft Dev", multipleJobs, multipleJobsData);
    }

    public void testNotFoundSearchResult() {
        testHelper("Random Text", emptyJob, emptyJobsData);
    }

    private void testHelper(String filterText, List<Job> filteredJobs, String[] filteredJobsData) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        when(api.getAllJobs()).thenReturn(Observable.fromArray());
        when(api.getAllJobs(filterText)).thenReturn(Observable.fromArray(filteredJobs));

        getActivity().SetAPI(api);
        solo.waitForView(R.id.JobsListView);

        solo.enterText((EditText) solo.getView(R.id.searchBox), filterText);
        TestHelper.findStrings(filteredJobsData, solo);

        verify(api, times(1)).getAllJobs();
        verify(api, times(1)).getAllJobs(filterText);
    }

    public void PopulateFakeJobs() {
        emptyJobsData = new String[]{};
        emptyJob = new ArrayList<>();
        allJobsData = new String[]{
                "Soft Dev", "Google",
                "Soft Dev", "Microsoft",
                "Soft Dev", "Facebook"
        };
        multipleJobsData = new String[]{
                "T1", "C1",
                "Title 2", "Company 2",
                "Software Engineering", "Facebook"
        };
        multipleJobs = new ArrayList<>();
        for (int i = 0; i < multipleJobsData.length; i += 2)
            multipleJobs.add(new JobBuilder().setTitle(multipleJobsData[i]).setOrganization(multipleJobsData[i + 1]).createJob());

        allJob = new ArrayList<>();
        for (int i = 0; i < allJobsData.length; i += 2)
            allJob.add(new JobBuilder().setTitle(allJobsData[i]).setOrganization(allJobsData[i + 1]).createJob());
    }
}