package intern.apply.internapply.model;

import java.util.List;

/**
 * Created by Roy on 2/4/2018.
 */

public class SurveyQuestion {
    private int id;
    private int questionIndex;
    private String question;
    private String questionType;
    private List<String> responses;

    public int getId() {
        return id;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public List<String> getResponses() {
        return responses;
    }
}
