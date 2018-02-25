package intern.apply.internapply.model;

import java.util.List;

public class SurveyQuestion {
    private int id;
    private int questionIndex;
    private String question;
    private String questionType;
    private List<String> responses;

    public SurveyQuestion(String question, int questionIndex, List<String> responses) {
        this.question = question;
        this.questionIndex = questionIndex;
        this.responses = responses;
    }

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
