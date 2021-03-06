package intern.apply.internapply.view.qnaactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.view.addquestionactivity.AddQuestionActivity;
import intern.apply.internapply.view.viewquestionactivity.ViewQuestionActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QNAActivity extends AppCompatActivity {

    private InternAPI api;
    private ListView questionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        onInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            getAllQuestions();
        }
    }

    private void onInit() {
        questionsListView = findViewById(R.id.questionsListView);
        onQuestionClick();
        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
        getAllQuestions();
    }

    /**
     * on question click handler,
     * Navigate to the question view
     */
    private void onQuestionClick() {
        questionsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Question question = (Question) adapterView.getItemAtPosition(i);

            Intent intent = new Intent(this, ViewQuestionActivity.class);
            intent.putExtra("questionId", question.getId());
            startActivity(intent);
        });
    }

    /**
     * Get all the questions from intern api and show them in a list
     */
    private void getAllQuestions() {
        api.getAllQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    QuestionsCustomListAdapter listAdapter = new QuestionsCustomListAdapter(this, response);
                    questionsListView.setAdapter(listAdapter);
                }, error -> {
                    Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                });
    }

    /**
     * on add button click, navigate to new question creation intent
     */
    public void addNewQuestion(View view) {
        Intent addJobIntent = new Intent(QNAActivity.this, AddQuestionActivity.class);
        startActivity(addJobIntent);
    }
}
