package intern.apply.internapply.view.surveyactivity;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SurveyList {
    private InternAPI api;
    private List<String> userAnswers;

    public SurveyList(InternAPI api) {
        this.api = api;
    }

    public void ShowList(SurveyActivity activity) {
        api.getSurvey().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    this.userAnswers = new ArrayList<String>(Collections.nCopies(response.size(), ""));
                    SurveyListAdapter listAdapter = new SurveyListAdapter(activity, response, this.userAnswers);
                    ListView listView = activity.findViewById(R.id.SurveyListView);
                    listView.setAdapter(listAdapter);
                }, error -> {
                    Toast.makeText(activity, "Internal server error, please try again later", Toast.LENGTH_LONG).show();
                    Log.i("error", error.toString());
                });
    }

    public List<String> getResponses(){ return userAnswers; }

}
