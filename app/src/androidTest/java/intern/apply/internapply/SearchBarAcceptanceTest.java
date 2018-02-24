package intern.apply.internapply;

import android.content.Intent;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.view.mainactivity.MainActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ohin on 2018-02-05.
 */

public class SearchBarAcceptanceTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND = "text not found";
    private Solo solo;


    private InternAPI api;
    private String[] multipleJobsData;
    private String[] emptyJobsData;
    private String[] allJobsData;


    private List<Job> multipleJobs;
    private List<Job> allJob;
    private List<Job> emptyJob;


    public SearchBarAcceptanceTest() {
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



    public void testEmptySearch(){
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(allJob);

        View searchView = solo.getView(R.id.searchBox);

        solo.enterText((EditText) solo.getView(R.id.searchBox),"");
        when(api.getAllJobs("")).thenReturn(output);

        getActivity().SetAPI(api,true);
        solo.waitForView(R.id.JobsListView);

        findStrings(allJobsData);

    }


    public void testFoundSearchResult(){

        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(multipleJobs);

        View searchView = solo.getView(R.id.searchBox);

        solo.enterText((EditText) solo.getView(R.id.searchBox),"Soft Dev");
        when(api.getAllJobs("Soft Dev")).thenReturn(output);

        getActivity().SetAPI(api,true);
        solo.waitForView(R.id.JobsListView);

        findStrings(multipleJobsData);

    }

    public void testNotFoundSearchResult(){

        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        Observable<List<Job>> output = Observable.fromArray(emptyJob);

        View searchView = solo.getView(R.id.searchBox);

        solo.enterText((EditText) solo.getView(R.id.searchBox),"Random Text");
        when(api.getAllJobs("Random Text")).thenReturn(output);

        getActivity().SetAPI(api,true);
        solo.waitForView(R.id.JobsListView);

        findStrings(emptyJobsData);

    }

    public void PopulateFakeJobs() {

        emptyJobsData = new String[]{};
        emptyJob = new ArrayList<>();
        allJobsData = new String[]{
                "Soft Dev","Google",
                "Soft Dev","Microsoft",
                "Soft Dev","Facebook"
        };
        multipleJobsData = new String[]{
                "T1", "C1",
                "Title 2", "Company 2",
                "Software Engineering", "Facebook"
        };
        multipleJobs = new ArrayList<>();
        for (int i = 0; i < multipleJobsData.length; i += 2)
            multipleJobs.add(new Job(multipleJobsData[i], multipleJobsData[i + 1]));

        allJob = new ArrayList<>();
        for (int i = 0; i < allJobsData.length; i += 2)
            allJob.add(new Job(allJobsData[i], allJobsData[i + 1]));



    }
}