package intern.apply.internapply.view.viewquestionactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.view.addansweractivity.AddAnswerActivity;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;
import intern.apply.internapply.view.qnaactivity.QuestionsCustomListAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewQuestionActivity extends AppCompatActivity {

    private int questionId;
    private InternAPI api;
    private TextView questionTitle;
    private TextView questionName;
    private TextView questionBody;
    private ListView answersListView;
    private TextView answersTitle;

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
        answersTitle = findViewById(R.id.tvAnswersTitle);
        answersListView = findViewById(R.id.answerList);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
            displayQuestion();
            displayAnswers();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
        displayQuestion();
        displayAnswers();
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
     * get the answers from the intent api and display them
     */
    private void displayAnswers(){
        api.getAnswers(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.size() > 0)
                        answersTitle.setVisibility(View.VISIBLE);

                    AnswersCustomListAdapter listAdapter = new AnswersCustomListAdapter(this, response);
                    answersListView.setAdapter(listAdapter);
                }, error -> {
                    Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
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

    /**
     * on add button click, navigate to new question creation intent
     */
    public void addNewAnswer(View view) {
        Intent addJobIntent = new Intent(ViewQuestionActivity.this, AddAnswerActivity.class);
        addJobIntent.putExtra("questionId", questionId);
        startActivity(addJobIntent);
    }
}
