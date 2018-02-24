package intern.apply.internapply.model;

public class Job {
    private int id;
    private final String organization;
    private final String title;
    private String location;
    private String description;
    private double salary;
    private int numSalaries;

    public Job(String title, String organization) {
        this.title = title;
        this.organization = organization;
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

    public double getSalary() {
        return salary;
    }

    public int getNumSalaries() {
        return numSalaries;
    }

    public String getDescription() {
        return description;
    }
}
