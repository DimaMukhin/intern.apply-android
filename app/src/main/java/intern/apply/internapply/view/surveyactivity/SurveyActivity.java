package intern.apply.internapply.view.surveyactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.CompletedSurvey;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Survey activity
 * Complete and send surveys
 */

public class SurveyActivity extends AppCompatActivity {

    private InternAPI api;
    private SurveyList questionList;

    private void onInit() {
        api = InternAPI.getAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        onInit();

        questionList = new SurveyList(api);
        questionList.ShowList(this);
    }

    /**
     * Send survey button click listener
     * sends a completed survey to the server.
     * displays success/error messages
     */

    public void sendSurvey(View view) {

        CompletedSurvey survey = new CompletedSurvey(questionList.getResponses());
        api.sendCompletedSurvey(survey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Toast.makeText(this, "Survey was sent successfully", Toast.LENGTH_LONG).show();
                }, error -> {
                    Log.i("error", error.toString());
                    Toast.makeText(this, "Internal server error, please try again later", Toast.LENGTH_LONG).show();
                });
    }
}