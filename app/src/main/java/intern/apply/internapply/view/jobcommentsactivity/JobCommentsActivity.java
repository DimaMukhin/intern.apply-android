package intern.apply.internapply.view.jobcommentsactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;

public class JobCommentsActivity extends AppCompatActivity {

    private int jobId;
    private InternAPI api;
    private ListView commentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_comments);
        onInit();
    }

    private void onInit() {
        getJobId();
        api = InternAPI.getAPI();

        commentsListView = findViewById(R.id.commentsListView);
    }

    /**
     * getting the intent that has the job id
     */
    private void getJobId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("jobId"))
                jobId = extras.getInt("jobId");
            else
                jobId = 0;
        }
    }
}
