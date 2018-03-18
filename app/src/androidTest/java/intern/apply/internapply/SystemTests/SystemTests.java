package intern.apply.internapply.SystemTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SystemTests
 * Contains all System tests.
 * Handles automatic running of all system tests.
 */

public class SystemTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(AddSalarySystemTest.class);
        suite.addTestSuite(JobListSystemTest.class);
        return suite;
    }
}
