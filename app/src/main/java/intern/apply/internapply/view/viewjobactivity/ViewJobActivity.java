package intern.apply.internapply.view.viewjobactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.view.jobcommentsactivity.JobCommentsActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewJobActivity extends AppCompatActivity {
    private int jobId;
    private InternAPI api;
    private TextView jobTitle;
    private TextView jobOrganization;
    private TextView jobLocation;
    private TextView jobDescription;
    private Button jobApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        onInit();
        displayJob();
    }

    private void onInit() {
        getJobId();
        api = InternAPI.getAPI();
        jobTitle = findViewById(R.id.jobTitle);
        jobOrganization = findViewById(R.id.jobOrganization);
        jobLocation = findViewById(R.id.jobLocation);
        jobDescription = findViewById(R.id.jobDescription);
        jobApply = findViewById(R.id.jobApply);
    }

    /**
     * displays a job in the view
     */
    private void displayJob() {
        api.getJob(jobId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.size() != 0) {
                        Job job = response.get(0);
                        jobTitle.setText(job.getTitle());
                        jobOrganization.setText(job.getOrganization());
                        jobLocation.setText(job.getLocation());
                        jobDescription.setText(job.getDescription());

                        // changed to visible here so rendering would be at the same time
                        jobApply.setVisibility(View.VISIBLE);

                        // making sure view is not taking any space if those are empty
                        jobOrganization.setVisibility(View.VISIBLE);
                        jobLocation.setVisibility(View.VISIBLE);
                    } else {
                        finish();
                    }
                }, error -> finish());
    }

    /**
     * "Comments" button click listener
     * Navigate to the comments section of the job
     */
    public void viewComments(View view) {
        Intent intent = new Intent(this, JobCommentsActivity.class);
        intent.putExtra("jobId", jobId);
        startActivity(intent);
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