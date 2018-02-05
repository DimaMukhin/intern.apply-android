package intern.apply.internapply.view.addjobactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Job;
import intern.apply.internapply.model.ServerError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddJobActivity extends AppCompatActivity {
    private EditText etJobOrg, etJobTitle, etJobDesc, etJobLoc;
    private InternAPI api;

    private void onInit() {
        etJobOrg = findViewById(R.id.etJobOrg);
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobDesc = findViewById(R.id.etJobDesc);
        etJobLoc = findViewById(R.id.etJobLoc);

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

    /**
     * Send the new job to the server.
     */
    public void addJob(View view) {

        String organization = etJobOrg.getText().toString();
        String title = etJobTitle.getText().toString();
        String description = etJobDesc.getText().toString();
        String location = etJobLoc.getText().toString();



        Job newJob = new Job(organization, title, location, description);

        api.addJob(newJob)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            resetForm();
                            Toast.makeText(this, "Job added successfully", Toast.LENGTH_LONG).show();
                        }, error -> {
                            Log.i("error", error.toString());
                            List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                            for (ServerError se : errors) {
                                if (se.getCode() == 11)
                                    Toast.makeText(this, "Invalid job organization", Toast.LENGTH_LONG).show();
                                else if (se.getCode() == 12)
                                    Toast.makeText(this, "Invalid job title (max 100 characters)", Toast.LENGTH_LONG).show();
                                else if (se.getCode() == 13)
                                    Toast.makeText(this, "Invalid job location (max 45 characters)", Toast.LENGTH_LONG).show();
                                else if (se.getCode() == 14)
                                    Toast.makeText(this, "Invalid job description (max 2000 characters)", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(this, "Internal server error, please try again later", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                );

    }

    private void resetForm() {
        etJobOrg.setText("");
        etJobTitle.setText("");
        etJobLoc.setText("");
        etJobDesc.setText("");
    }
}
