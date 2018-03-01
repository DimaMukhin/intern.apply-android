package intern.apply.internapply.model;

public class JobBuilder {
    private String title = "";
    private String organization = "";
    private String location = "";
    private String description = "";
    private double salary = 0;
    private int numSalaries = 0;

    public JobBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public JobBuilder setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public JobBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public JobBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public JobBuilder setSalary(double salary) {
        this.salary = salary;
        return this;
    }

    public JobBuilder setNumSalaries(int numSalaries) {
        this.numSalaries = numSalaries;
        return this;
    }

    public Job createJob() {
        return new Job(organization, title, location, description, salary, numSalaries);
    }
}