package intern.apply.internapply.model;

public class Salary {
    private int jobID;
    private String salary;
    private int salaryType;

    public Salary(int id, String salary, int salaryType) {
        this.jobID = id;
        this.salary = salary;
        this.salaryType = salaryType;
    }

    public String getSalary() {
        return salary;
    }

    public int getSalaryType() {
        return salaryType;
    }
}
