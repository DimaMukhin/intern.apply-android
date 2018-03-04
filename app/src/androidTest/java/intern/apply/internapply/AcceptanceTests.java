package intern.apply.internapply;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AcceptanceTests
 * Contains all acceptance tests.
 * Handles automatic running of all acceptance tests.
 */

public class AcceptanceTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(AddJobAcceptanceTest.class);
        suite.addTestSuite(AddJobCommentAcceptanceTest.class);
        suite.addTestSuite(AddSalaryAcceptanceTest.class);
        suite.addTestSuite(ContactUsAcceptanceTest.class);
        suite.addTestSuite(JobCommentsAcceptanceTest.class);
        suite.addTestSuite(JobListAcceptanceTest.class);
        suite.addTestSuite(RateJobAcceptanceTest.class);
        suite.addTestSuite(SearchBarAcceptanceTest.class);
        suite.addTestSuite(SurveyAcceptanceTest.class);
        suite.addTestSuite(ViewJobAcceptanceTests.class);
        return suite;
    }
}
