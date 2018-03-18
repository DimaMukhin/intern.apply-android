package intern.apply.internapply.view.addjobactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.api.InternAPIProvider;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.JobBuilder;
import intern.apply.internapply.model.ServerError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddJobActivity extends AppCompatActivity {
    private EditText etJobOrg, etJobTitle, etJobLoc, etJobUrl, etJobDesc;
    private InternAPIProvider api;

    private void onInit() {
        etJobOrg = findViewById(R.id.etJobOrg);
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobDesc = findViewById(R.id.etJobDesc);
        etJobLoc = findViewById(R.id.etJobLoc);
        etJobUrl = findViewById(R.id.etJobUrl);

        api = InternAPI.getAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        //To get the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onInit();
    }

    public void setApi(InternAPIProvider api) {
        this.api = api;
    }

    /**
     * Send the new job to the server.
     */
    public void addJob(View view) {

        String organization = etJobOrg.getText().toString();
        String title = etJobTitle.getText().toString();
        String location = etJobLoc.getText().toString();
        String url = etJobUrl.getText().toString();
        String description = etJobDesc.getText().toString();

        Job newJob = new JobBuilder()
                .setOrganization(organization)
                .setTitle(title)
                .setLocation(location)
                .setUrl(url)
                .setDescription(description)
                .createJob();

        api.addJob(newJob)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            resetForm();
                            Toast.makeText(this, "Job added successfully", Toast.LENGTH_LONG).show();
                        }, error -> {
                            List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                            if (errors.size() == 0 || errors.get(0).getCode() == 0)
                                Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                            else {
                                if (errors.get(0).getCode() == 11)
                                    Toast.makeText(this, R.string.InvalidJobOrganization, Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 12)
                                    Toast.makeText(this, R.string.InvalidJobTitle, Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 13)
                                    Toast.makeText(this, R.string.InvalidJobLocation, Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 14)
                                    Toast.makeText(this, R.string.InvalidJobDescription, Toast.LENGTH_LONG).show();
                                else if (errors.get(0).getCode() == 15)
                                    Toast.makeText(this, R.string.InvalidJobUrl, Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void resetForm() {
        etJobOrg.setText("");
        etJobTitle.setText("");
        etJobLoc.setText("");
        etJobUrl.setText("");
        etJobDesc.setText("");
    }
}