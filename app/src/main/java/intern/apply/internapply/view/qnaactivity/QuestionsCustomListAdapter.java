package intern.apply.internapply.view.qnaactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.Question;

/**
 * List adapter for a list of questions
 */

public class QuestionsCustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<Question> questions;

    public QuestionsCustomListAdapter(Activity context, List<Question> questions) {
        super(context, R.layout.questions_listview_row, questions);

        this.context = context;
        this.questions = questions;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.questions_listview_row, null, true);

        TextView nameTextField = rowView.findViewById(R.id.tvQuestionName);
        TextView titleTextField = rowView.findViewById(R.id.tvQuestionTitle);

        nameTextField.setText(questions.get(position).getAuthor());
        titleTextField.setText(questions.get(position).getTitle());

        return rowView;
    }
}
