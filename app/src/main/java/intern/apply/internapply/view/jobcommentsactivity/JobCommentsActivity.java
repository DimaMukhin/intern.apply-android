package intern.apply.internapply.view.jobcommentsactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        getAllComments();
    }

    /**
     * get and display all comments
     */
    private void getAllComments() {
        api.getJobComments(jobId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    CommentsCustomListAdapter listAdapter = new CommentsCustomListAdapter(this, response);
                    commentsListView.setAdapter(listAdapter);
                }, error -> {
                    Toast.makeText(this, "Internal server error, please try again later", Toast.LENGTH_LONG).show();
                });
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
