package intern.apply.internapply.model;

public class ContactMessage {
    private String email;
    private String title;
    private String message;

    public ContactMessage() {}

    public ContactMessage(String email, String title, String message) {
        this.email = email;
        this.title = title;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
