package intern.apply.internapply.model;

/**
 * Q&A-Answer data model
 */

public class Answer {
    private String author;
    private String body;

    public Answer(String author, String body){
        this.body = body;
        this.author = author;
    }

    public String getBody(){
        return body;
    }

    public String getAuthor(){
        return author;
    }
}
