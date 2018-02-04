package intern.apply.internapply.model;

import java.util.List;

/**
 * Created by Roy on 2/5/2018.
 */

public class CompletedSurvey {
    private List<String> answers;

    public CompletedSurvey(List<String> responses) {
        this.answers = responses;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
