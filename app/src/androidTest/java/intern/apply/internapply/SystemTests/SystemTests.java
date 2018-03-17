package intern.apply.internapply.SystemTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SystemTests
 * Contains all acceptance tests.
 * Handles automatic running of all acceptance tests.
 */

public class SystemTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(AddSalarySystemTest.class);
        //suite.addTestSuite(JobListAcceptanceTest.class);
        return suite;
    }
}
