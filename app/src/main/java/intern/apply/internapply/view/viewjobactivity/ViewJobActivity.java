package intern.apply.internapply.view.viewjobactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.ServerError;
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

    private EditText etName;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        onInit();
    }

    private void onInit() {
        getJobId();
        jobTitle = findViewById(R.id.jobTitle);
        jobOrganization = findViewById(R.id.jobOrganization);
        jobLocation = findViewById(R.id.jobLocation);
        jobDescription = findViewById(R.id.jobDescription);
        jobApply = findViewById(R.id.jobApply);
        etName = findViewById(R.id.etName);
        etMessage = findViewById(R.id.etMessageComment);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
            displayJob();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
        displayJob();
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
     * add a comment to the job
     */
    public void sendMessage(View view) {
        String name = etName.getText().toString();
        String message = etMessage.getText().toString();

        Comment newComment = new Comment(jobId, message, name);
        api.addJobComment(newComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> Toast.makeText(this, R.string.CommentSuccess, Toast.LENGTH_LONG).show()
                        , error -> {
                            List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                            if (errors.size() == 0 || errors.get(0).getCode() == 0 || errors.get(0).getCode() == 4)
                                Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                            else {
                                if (errors.get(0).getCode() == 5)
                                    Toast.makeText(this, R.string.InvalidCommentBody, Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 6)
                                    Toast.makeText(this, R.string.InvalidCommentName, Toast.LENGTH_LONG).show();
                            }
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
