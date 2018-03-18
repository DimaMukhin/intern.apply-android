package intern.apply.internapply.SystemTests;

import junit.framework.AssertionFailedError;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class TestDBHelper {

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
        ExecuteSQL(sql);

        sql = "DROP TABLE IF EXISTS comment";
        ExecuteSQL(sql);

        sql = "DROP TABLE IF EXISTS job";
        ExecuteSQL(sql);

        sql = " CREATE TABLE job (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "organization VARCHAR(45) NOT NULL," +
                "title VARCHAR(100) NOT NULL," +
                "location VARCHAR(45)," +
                "description VARCHAR(2000)," +
                "salary DECIMAL(4,1)," +
                "numSalaries INT(10)," +
                "PRIMARY KEY (id))";
        ExecuteSQL(sql);

        sql = "CREATE TABLE comment (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "jobID INT NOT NULL," +
                "message VARCHAR(300) NOT NULL," +
                "author VARCHAR(45) NOT NULL," +
                "ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (jobID) REFERENCES job (id))";
        ExecuteSQL(sql);

        sql = "CREATE TABLE jobRating (" +
                "jobId INT(11) NOT NULL," +
                "score DECIMAL(3,2) DEFAULT '0.00' NOT NULL," +
                "votes INT(11) DEFAULT '0' NOT NULL," +
                "PRIMARY KEY(jobId)," +
                "CONSTRAINT jobId___fk FOREIGN KEY (jobId) REFERENCES job (id) ON DELETE CASCADE )";
        ExecuteSQL(sql);
    }

    public static void createContactUsTable() {
        String sql;

        sql = "DROP TABLE IF EXISTS contactMessage";
        ExecuteSQL(sql);

        sql = "CREATE TABLE contactMessage (" +
                "id INT NOT NULL AUTO_INCREMENT," +
                "email VARCHAR(45) NOT NULL," +
                "title VARCHAR(45) NOT NULL," +
                "message VARCHAR(300) NOT NULL," +
                "PRIMARY KEY (id))";
        ExecuteSQL(sql);
    }

    public static void InitializeJobTables() {

        String sql;

        sql = "INSERT INTO job (id, organization, title, location, salary, numSalaries) VALUES" +
                "(1, 'fake org', 'fake title', 'fake location', 0, 0)," +
                "(2, 'google', 'second title', 'vancouver', 0, 0)," +
                "(3, 'CityOFWinnipeg', 'third title', 'location', 0, 0)";
        ExecuteSQL(sql);

        sql = "INSERT INTO jobRating(jobId, score, votes) VALUES" +
                "(1, 1.0, 1)," +
                "(2, 2.0, 2)";
        ExecuteSQL(sql);

        sql = "INSERT INTO comment (id, jobID, message, author) VALUES" +
                "(1, 1, 'this is a nice comment body', 'dima')," +
                "(2, 1, 'another comment for the same job', 'ben')," +
                "(3, 2, 'this last comment is for job 2', 'rick')";
        ExecuteSQL(sql);
    }

    public static void CleanTables() {
        String sql;

        sql = "Delete from jobRating";
        ExecuteSQL(sql);

        sql = "Delete from comment";
        ExecuteSQL(sql);

        sql = "Delete from job";
        ExecuteSQL(sql);

        sql = "Delete from contactMessage";
        ExecuteSQL(sql);
    }
}
