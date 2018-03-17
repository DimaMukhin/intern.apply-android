package intern.apply.internapply.SystemTests;

import com.robotium.solo.Solo;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class TestHelper {

    static String LOCAL_HOST_URL = "http://192.168.0.20:3000/";

    public static void findStrings(String[] expectedStrings, Solo solo) {
        for (String s : expectedStrings)
            Assert.assertTrue("text not found", solo.waitForText(s));
    }

    public static void ExecuteSQL(String sql) {
        if (sql.isEmpty())
            return;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://fugfonv8odxxolj8.cbetxkdyhwsb.us-east-1.rds.amazonaws.com/x9ptoxf7hkxdbkme";
            Connection c = DriverManager.getConnection(url, "rziicv90jjsju3xj", "eso1lssuop8145gk");
            PreparedStatement st = c.prepareStatement(sql);
            st.execute();
            st.close();
            c.close();
        } catch (Exception e) {
            throw new AssertionFailedError("SQL QUERY FAILED:" + e.toString());
        }
    }

    public static void CreateJobTables() {
        String sql = "DROP TABLE IF EXISTS jobRating";
        TestHelper.ExecuteSQL(sql);

        sql = "DROP TABLE IF EXISTS job";
        TestHelper.ExecuteSQL(sql);

        sql = " CREATE TABLE job (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "organization VARCHAR(45) NOT NULL," +
                "title VARCHAR(100) NOT NULL," +
                "location VARCHAR(45)," +
                "description VARCHAR(2000)," +
                "salary DECIMAL(4,1)," +
                "numSalaries INT(10)," +
                "PRIMARY KEY (id))";
        TestHelper.ExecuteSQL(sql);

        sql = "CREATE TABLE jobRating (" +
                "jobId INT(11) NOT NULL," +
                "score DECIMAL(3,2) DEFAULT '0.00' NOT NULL," +
                "votes INT(11) DEFAULT '0' NOT NULL," +
                "PRIMARY KEY(jobId)," +
                "CONSTRAINT jobId___fk FOREIGN KEY (jobId) REFERENCES job (id) ON DELETE CASCADE )";
        TestHelper.ExecuteSQL(sql);
    }

    public static void InitializeJobTables() {

        String sql;

        sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'fake org', 'fake title', 'fake location', 0, 0)," +
                "(2, 'google', 'second title', 'vancouver', 0, 0)," +
                "(3, 'CityOFWinnipeg', 'third title', 'location', 0, 0)";
        TestHelper.ExecuteSQL(sql);

        sql = "INSERT INTO jobRating(jobId, score, votes) VALUES" +
                "(1, 1.0, 1)," +
                "(2, 2.0, 2)";
        TestHelper.ExecuteSQL(sql);
    }

    public static void CleanTables() {
        String sql;

        sql = "Delete from jobRating";
        TestHelper.ExecuteSQL(sql);

        sql = "Delete from job";
        TestHelper.ExecuteSQL(sql);
    }
}
