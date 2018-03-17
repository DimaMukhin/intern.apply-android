package intern.apply.internapply;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllIntegrationTests
 * Contains all integration tests.
 * Handles automatic running of all integration tests.
 */

public class AllIntegrationTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Integration tests");
        suite.addTestSuite(AddAnswerIntegrationTest.class);
        suite.addTestSuite(AddJobCommentIntegrationTest.class);
        suite.addTestSuite(AddJobIntegrationTest.class);
        suite.addTestSuite(AddQuestionIntegrationTest.class);
        suite.addTestSuite(AddSalaryIntegrationTest.class);
        suite.addTestSuite(AnswerListIntegrationTest.class);
        suite.addTestSuite(ContactUsIntegrationTest.class);
        suite.addTestSuite(JobCommentsIntegrationTest.class);
        suite.addTestSuite(JobListIntegrationTest.class);
        suite.addTestSuite(QuestionListIntegrationTest.class);
        suite.addTestSuite(RateJobIntegrationTest.class);
        suite.addTestSuite(SearchBarIntegrationTest.class);
        suite.addTestSuite(SurveyIntegrationTest.class);
        suite.addTestSuite(ViewJobIntegrationTests.class);
        suite.addTestSuite(ViewQuestionIntegrationTest.class);
        return suite;
    }
}
