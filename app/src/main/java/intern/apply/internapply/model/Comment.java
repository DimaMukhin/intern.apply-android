package intern.apply.internapply.model;

public class Comment {

    private int id;
    private int jobID;
    private String message;
    private String author;
    private String ts;

    public Comment() {

    }

    public Comment(int jobID, String message, String author) {
        this.jobID = jobID;
        this.message = message;
        this.author = author;
    }

    public Comment(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public int getJobID() {
        return jobID;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getTs() {
        return ts;
    }
}
