package intern.apply.internapply.view.viewquestionactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.Answer;
import intern.apply.internapply.model.Question;

/**
 * List adapter for a list of answers
 */

public class AnswersCustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<Answer> answers;

    public AnswersCustomListAdapter(Activity context, List<Answer> answers) {
        super(context, R.layout.answers_listview_row, answers);

        this.context = context;
        this.answers = answers;
    }

    public View getView(int position, View view, ViewGroup parent) {
        String by = context.getString(R.string.byAuthor) + " ";
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.answers_listview_row, null,true);

        TextView authorTextField = (TextView) rowView.findViewById(R.id.tvAnswerAuthor);
        TextView bodyTextField = (TextView) rowView.findViewById(R.id.answerBody);

        authorTextField.setText(by + answers.get(position).getAuthor());
        bodyTextField.setText(answers.get(position).getBody());

        return rowView;
    }
}
