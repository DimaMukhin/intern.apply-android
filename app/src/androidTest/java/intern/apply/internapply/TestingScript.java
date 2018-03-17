package intern.apply.internapply;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.view.mainactivity.MainActivity;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestingScript extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ACTIVITY_ERROR = "wrong activity";
    private final InternAPIProvider api;
    private Solo solo;
    private String[] singleJobData;
    private String[] multipleJobsData;
    private List<Job> singleJob;
    private List<Job> multipleJobs;

    public TestingScript() throws Exception {
        super(MainActivity.class);
        api = mock(InternAPIProvider.class);
        setActivityIntent(new Intent().putExtra("TEST", true));
        PopulateFakeJobs();
        //executeCommand();

        //DBConnector.ExecuteSQL("UPDATE job SET location = 'wasdf' WHERE id = 1;");
    }

    public static void executeCommand() throws Exception {
        try {
            // Run the process
            Process p = Runtime.getRuntime().exec("cmd /c scripts\\test.bat");
            // Get the input stream
            InputStream is = p.getInputStream();

            OutputStream a = p.getOutputStream();
            //a.write("asdf");

            // Read script execution results
            int i = 0;
            StringBuffer sb = new StringBuffer();
            while ((i = is.read()) != -1)
                sb.append((char) i);

            Log.d("executeCommand", sb.toString());

            is = p.getErrorStream();

            // Read script execution results
            i = 0;
            sb = new StringBuffer();
            while ((i = is.read()) != -1)
                sb.append((char) i);

            Log.d("executeCommand", sb.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("executeCommand", "End");
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
