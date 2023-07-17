package org.vaadin.example.models;

public class EmpDep {
    private int empId;

    private String empName;

    private int depId;

    private String depName;

    private int jobId;

    private String jobName;

    private String EmpPhone;

    private String EmpEmail;

    private String DepEmail;

    private String depPhone;

    private String JobSalary;

    public EmpDep(int empId, String empName, int depId, String depName,
                  int jobId, String jobName,String EmpPhone,String EmpEmail,String DepEmail,String depPhone,String JobSalary){
        this.empId = empId;
        this.empName = empName;
        this.depId = depId;
        this.depName = depName;
        this.jobId = jobId;
        this.jobName = jobName;
        this.EmpPhone =EmpPhone;
        this.EmpEmail =EmpEmail;
        this.DepEmail =DepEmail;
        this.depPhone =depPhone;
        this.JobSalary =JobSalary;

    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getEmpPhone() {
        return EmpPhone;
    }

    public void setEmpPhone(String empPhone) {
        EmpPhone = empPhone;
    }

    public String getEmpEmail() {
        return EmpEmail;
    }

    public void setEmpEmail(String empEmail) {
        EmpEmail = empEmail;
    }

    public String getDepEmail() {
        return DepEmail;
    }

    public void setDepEmail(String depEmail) {
        DepEmail = depEmail;
    }

    public String getDepPhone() {
        return depPhone;
    }

    public void setDepPhone(String depPhone) {
        this.depPhone = depPhone;
    }

    public String getJobSalary() {
        return JobSalary;
    }

    public void setJobSalary(String jobSalary) {
        JobSalary = jobSalary;
    }
}
