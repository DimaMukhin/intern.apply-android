package intern.apply.internapply.view.addansweractivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Answer;
import intern.apply.internapply.model.ServerError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddAnswerActivity extends AppCompatActivity {

    private InternAPI api;
    private int questionId;
    private EditText etAnswerAuthor;
    private EditText etAnswerBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        onInit();
    }

    private void onInit() {
        getQuestionId();
        etAnswerAuthor = findViewById(R.id.etAnswerAuthor);
        etAnswerBody = findViewById(R.id.etAnswerBody);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
    }

    public void addNewAnswer(View view) {
        String author = etAnswerAuthor.getText().toString();
        String body = etAnswerBody.getText().toString();

        Answer newAnswer = new Answer(author, body);
        api.addAnswer(questionId, newAnswer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Toast.makeText(this, R.string.answerSuccess, Toast.LENGTH_LONG).show();
                    finish();
                }, error -> {
                    List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                    if (errors.size() == 0 || errors.get(0).getCode() == 0)
                        Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                    else {
                        if (errors.get(0).getCode() == 33)
                            Toast.makeText(this, R.string.invalidAnswerAuthor, Toast.LENGTH_LONG).show();
                        else if (errors.get(0).getCode() == 34)
                            Toast.makeText(this, R.string.invalidAnswerBody, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * get the id of the question
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
