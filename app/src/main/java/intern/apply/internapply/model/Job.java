package intern.apply.internapply.model;

/**
 * Created by Unknown on 2018-01-19.
 */

public class Job {
    private String title;
    private String organization;
    private String location;
    private String description;
    private int id;

    public Job(String title, String organization, String location, String description, int id) {
        this.title = title;
        this.organization = organization;
        this.location = location;
        this.description = description;
        this.id = id;
    }

    public String getCompanyName() {
        return organization;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() { return location; }

    public String getDescription() { return description; }

    public int getId() { return id; }
}
