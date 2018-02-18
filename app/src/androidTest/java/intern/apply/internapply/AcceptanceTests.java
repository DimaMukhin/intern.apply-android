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
        suite.addTestSuite(JobListAcceptanceTest.class);
        suite.addTestSuite(ContactUsAcceptanceTest.class);
        suite.addTestSuite(AddJobCommentAcceptanceTest.class);
        suite.addTestSuite(JobCommentsAcceptanceTest.class);
        return suite;
    }
}
