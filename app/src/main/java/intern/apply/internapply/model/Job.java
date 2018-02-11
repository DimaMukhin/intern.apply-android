package intern.apply.internapply.model;

public class Job {
    private String title;
    private String organization;

    public Job(String title, String organization) {
        this.title = title;
        this.organization = organization;
    }

    public String getCompanyName() {
        return organization;
    }

    public String getTitle() {
        return title;
    }
}
