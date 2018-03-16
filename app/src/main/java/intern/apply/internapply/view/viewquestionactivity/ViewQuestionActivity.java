package intern.apply.internapply.view.viewquestionactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewQuestionActivity extends AppCompatActivity {

    private int questionId;
    private InternAPI api;
    private TextView questionTitle;
    private TextView questionName;
    private TextView questionBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        onInit();
    }

    private void onInit() {
        getQuestionId();
        questionTitle = findViewById(R.id.tvQuestionViewTitle);
        questionName = findViewById(R.id.tvQuestionViewName);
        questionBody = findViewById(R.id.tvQuestionViewBody);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
            displayQuestion();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
        displayQuestion();
    }

    /**
     * get the question from intern api and display it
     */
    private void displayQuestion() {
        api.getQuestion(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!(response instanceof Question)) {
                        Toast.makeText(this, R.string.couldNotGetQuestion, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    Question question = response;
                    questionTitle.setText(question.getTitle());
                    questionName.setText(question.getAuthor());
                    questionBody.setText(question.getBody());
                }, error -> {
                    Toast.makeText(this, R.string.couldNotGetQuestion, Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    /**
     * get the id of the question to display
     */
    private void getQuestionId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("questionId"))
                questionId = extras.getInt("questionId");
            else
                questionId = 0;
        }
    }
}
