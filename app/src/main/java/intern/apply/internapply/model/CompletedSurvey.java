package intern.apply.internapply.model;

import java.util.List;

public class CompletedSurvey {
    private List<String> answers;

    public CompletedSurvey(List<String> responses) {
        this.answers = responses;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
