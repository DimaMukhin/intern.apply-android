package intern.apply.internapply.view.surveyactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import intern.apply.internapply.R;
import intern.apply.internapply.model.SurveyQuestion;


public class SurveyListAdapter extends ArrayAdapter {
    private static final int defaultValSelect = 0;

    //to reference the Activity
    private final Activity context;
    private final List<SurveyQuestion> questions;
    private LinearLayout.LayoutParams param;
    private List<String> userAnswers;

    public SurveyListAdapter(Activity context, List<SurveyQuestion> questions, List<String> userAnswers) {
        super(context, R.layout.jobs_listview_row, questions);
        this.context = context;
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.survey_question_row, null, true);

        addRadioButtons(position, rowView);

        TextView titleField = rowView.findViewById(R.id.QuestionTextView);

        titleField.setText(position+1 + ". " + questions.get(position).getQuestion());

        return rowView;
    }

    public RadioGroup addRadioButtons(int position, View rowView) {
        RadioGroup radioGroup = new RadioGroup(context);

        List<String> responses = questions.get(position).getResponses();
        LinearLayout layout = rowView.findViewById(R.id.questionRow);

        layout.addView(radioGroup, param);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = rowView.findViewById(checkedId);
                        userAnswers.set(position, radioButton.getText().toString());
                    }
                }
        );

        for(int i = 0; i < responses.size(); i++) {
            RadioButton radioButtonView = new RadioButton(context);
            radioButtonView.setText(responses.get(i));
            radioGroup.addView(radioButtonView, param);

            if (responses.get(i).equals(userAnswers.get(position)) || (userAnswers.get(position).equals("") && i == defaultValSelect))
                radioButtonView.setChecked(true);
        }

        return radioGroup;
    }

}
