package intern.apply.internapply.model;

public class Job {
    private int id;
    private String organization;
    private String title;
    private String location;
    private String description;

    public Job(String title, String organization) {
        this.title = title;
        this.organization = organization;
    }

    public Job(String title, String organization, String location, String description, int id) {
        this.title = title;
        this.organization = organization;
        this.location = location;
        this.description = description;
        this.id = id;
    }

    public Job(String organization, String title, String location, String description) {
        this.organization = organization;
        this.title = title;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
