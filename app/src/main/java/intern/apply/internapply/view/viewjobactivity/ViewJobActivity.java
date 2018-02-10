package intern.apply.internapply.view.viewjobactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.api.InternAPIClient;
import intern.apply.internapply.view.mainactivity.CustomListAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewJobActivity extends AppCompatActivity {
    private int jobId;
    private InternAPI api;
    private TextView jobTitle;
    private TextView jobOrgnization;
    private TextView jobLocation;
    private TextView jobDescription;
    private Button jobApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);
        onInit();
    }

    private void onInit(){
        getJobId();
        api = InternAPI.getAPI();
        jobTitle = findViewById(R.id.jobTitle);
        jobOrgnization = findViewById(R.id.jobOrganization);
        jobLocation = findViewById(R.id.jobLocation);
        jobDescription = findViewById(R.id.jobDescription);
        jobApply = findViewById(R.id.jobApply);
    }

    /**
     * displays a job in the view
     */
    private void displayJob(){
        api.getJob(jobId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    jobTitle.setText(response.getTitle());
                    jobOrgnization.setText(response.getCompanyName());
                    jobLocation.setText(response.);
                }, error -> {
                    Toast.makeText(this, "Internal server error, please try again later", Toast.LENGTH_LONG).show()
                });
    }

    /**
     * checking if intent exists
     * @return true if intent exists
     */
    private boolean getJobId(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            if(extras.containsKey("jobId"))
                jobId = extras.getInt("jobId");
            else
                jobId = 0;
        }
    }
}
