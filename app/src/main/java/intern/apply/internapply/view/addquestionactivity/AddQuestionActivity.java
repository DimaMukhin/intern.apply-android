package intern.apply.internapply.view.addquestionactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.api.InternAPI;
import intern.apply.internapply.model.Question;
import intern.apply.internapply.model.ServerError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddQuestionActivity extends AppCompatActivity {

    private InternAPI api;
    private EditText etName;
    private EditText etTitle;
    private EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        onInite();
    }

    private void onInite() {
        etName = findViewById(R.id.etQuestionName);
        etTitle = findViewById(R.id.etQuestionTitle);
        etBody = findViewById(R.id.etQuestionBody);

        boolean test = getIntent().getBooleanExtra("TEST", false);
        if (!test) {
            api = InternAPI.getAPI();
        }
    }

    public void setApi(InternAPI api) {
        this.api = api;
    }

    /**
     * Add a new question to the Q&A board
     */
    public void addNewQuestion(View view) {
        String name = etName.getText().toString();
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        Question newQuestion = new Question(title, body, name);
        api.addNewQuestion(newQuestion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Toast.makeText(this, R.string.questionSuccess, Toast.LENGTH_LONG).show();
                    finish();
                }, error -> {
                    List<ServerError> errors = ServerError.getErrorsFromServerException(error);

                    if (errors.size() == 0 || errors.get(0).getCode() == 0)
                        Toast.makeText(this, R.string.InternalServerError, Toast.LENGTH_LONG).show();
                    else {
                        if (errors.get(0).getCode() == 7)
                            Toast.makeText(this, R.string.invalidQuestionTitle, Toast.LENGTH_LONG).show();
                        else if (errors.get(0).getCode() == 8)
                            Toast.makeText(this, R.string.invalidQuestionBody, Toast.LENGTH_LONG).show();
                        else if (errors.get(0).getCode() == 9)
                            Toast.makeText(this, R.string.invalidQuestionAuthor, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
