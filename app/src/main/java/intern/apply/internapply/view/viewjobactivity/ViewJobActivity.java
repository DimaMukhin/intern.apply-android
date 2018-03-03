package intern.apply.internapply.view.viewjobactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Comment;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobRating;
import intern.apply.internapply.model.Salary;
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
    private TextView jobSalary;
    private TextView jobDescription;
    private TextView votes;
    private Button jobApply;
    private RatingBar rating;

    private EditText etName;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
            onInit();
        }
    }

    private void onInit() {
        getJobId();
        jobTitle = findViewById(R.id.jobTitle);
        jobOrganization = findViewById(R.id.jobOrganization);
        jobLocation = findViewById(R.id.jobLocation);
        jobSalary = findViewById(R.id.jobSalary);
        jobDescription = findViewById(R.id.jobDescription);
        jobApply = findViewById(R.id.jobApply);
        etName = findViewById(R.id.etName);
        etMessage = findViewById(R.id.etMessageComment);
        rating = findViewById(R.id.ratingBar);
        votes = findViewById(R.id.votes);
        setupDialogForSalary();
        displayJob();
        displayJobRating();
    }


    public void setApi(InternAPI api) {
        this.api = api;
        onInit();
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

                                if (job.getNumSalaries() > 0)
                                    jobSalary.setText("Average Salary: " + job.getSalary() + "k per year (" + job.getNumSalaries() + " submits)");
                                else
                                    jobSalary.setVisibility(View.GONE);

                                jobDescription.setText(job.getDescription());

                                // changed to visible here so rendering would be at the same time
                                jobApply.setVisibility(View.VISIBLE);

                                // making sure view is not taking any space if those are empty
                                jobOrganization.setVisibility(View.VISIBLE);
                                jobLocation.setVisibility(View.VISIBLE);
                            } else {
                                finish();
                            }
                        }, error -> finish()
                );
    }

    /**
     * displays job's rating
     */
    private void displayJobRating() {
        // initial rating
        api.getJobRating(jobId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.size() > 0)
                        setRating(response.get(0));
                    else
                        setRating(new JobRating(0, 0));
                }, error -> {
                    Toast.makeText(this, R.string.ratingError, Toast.LENGTH_LONG).show();
                });

        rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                JobRating jobRating = new JobRating(Math.round(rating));
                api.rateJob(jobId, jobRating).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((JobRating response) -> {
                            Toast.makeText(this, R.string.ratedSuccessfully, Toast.LENGTH_LONG).show();
                            api.getJobRating(jobId).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response2 -> {
                                        if (response2.size() > 0)
                                            setRating(response2.get(0));
                                        else
                                            setRating(new JobRating(0, 0));
                                    }, error ->
                                            Toast.makeText(this, R.string.ratingError, Toast.LENGTH_LONG).show());
                        }, err -> Toast.makeText(this, R.string.ratingError, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setRating(JobRating jobRating) {
        rating.setRating(Math.round(jobRating.getScore()));
        votes.setText(jobRating.getVotes() + " " + getResources().getString(R.string.votes));
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

    private void setupDialogForSalary() {
        Button salaryDialog = findViewById(R.id.salaryButton);
        salaryDialog.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_salary, null);
            EditText salaryInput = mView.findViewById(R.id.salaryInput);

            Spinner dropdown = mView.findViewById(R.id.salaryTypeInput);
            String[] items = new String[]{"Choose a pay duration", "Yearly", "Monthly", "Biweekly", "Hourly"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewJobActivity.this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

            Button salarySumbit = mView.findViewById(R.id.salarySubmit);
            builder.setView(mView);
            AlertDialog dialog = builder.create();
            dialog.show();

            salarySumbit.setOnClickListener(view1 -> {
                Salary newSalary = new Salary(jobId, salaryInput.getText().toString(), dropdown.getSelectedItemPosition() - 1);
                api.addJobSalary(newSalary)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response ->
                                {
                                    jobSalary.setVisibility(View.VISIBLE);
                                    jobSalary.setText("Average Salary: " + response.getSalary() + "k per year (" + response.getSalaryType() + " submits)");
                                    dialog.dismiss();
                                    Toast.makeText(ViewJobActivity.this, R.string.SalarySuccess, Toast.LENGTH_LONG).show();
                                }
                                , error -> {
                                    List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                                    if (errors.size() == 0 || errors.get(0).getCode() == 0 || errors.get(0).getCode() == 4)
                                        Toast.makeText(ViewJobActivity.this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                                    else {
                                        if (errors.get(0).getCode() == 41)
                                            Toast.makeText(ViewJobActivity.this, R.string.InvalidSalary, Toast.LENGTH_LONG).show();
                                        else if (errors.get(0).getCode() == 42)
                                            Toast.makeText(ViewJobActivity.this, R.string.InvalidSalaryType, Toast.LENGTH_LONG).show();
                                    }
                                });
            });

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
