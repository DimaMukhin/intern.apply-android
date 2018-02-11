package intern.apply.internapply;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AcceptanceTests
 * Contains all acceptance tests.
 * Handles automatic running of all acceptance tests.
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2018-Feb-03
 */

public class AcceptanceTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(JobListAcceptanceTest.class);
        suite.addTestSuite(ContactUsAcceptanceTest.class);
        return suite;
    }
}
