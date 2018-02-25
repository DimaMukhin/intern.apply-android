package intern.apply.internapply.view.surveyactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.CompletedSurvey;
import intern.apply.internapply.model.ServerError;
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
        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            setApi(InternAPI.getAPI());
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
        questionList = new SurveyList(api);
        questionList.ShowList(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        onInit();
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
                    Toast.makeText(this, R.string.SurveySuccess, Toast.LENGTH_LONG).show();
                }, error -> {
                    List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                    if (errors.size() == 0 || errors.get(0).getCode() == 0)
                        Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                    else if (errors.get(0).getCode() == 51)
                        Toast.makeText(this, R.string.InvalidSurvey, Toast.LENGTH_LONG).show();
                });
    }
}