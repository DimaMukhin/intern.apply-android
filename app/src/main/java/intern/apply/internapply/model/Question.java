package intern.apply.internapply.model;

/**
 * Created by Unknown on 2018-02-26.
 */

public class Question {

    private int id;
    private String title;
    private String body;
    private String author;
    private String creationTime;

    public Question() {

    }

    public Question(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Question(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreationTime() {
        return creationTime;
    }
}
