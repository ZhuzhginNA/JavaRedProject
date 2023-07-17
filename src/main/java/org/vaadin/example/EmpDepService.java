package org.vaadin.example;

import org.springframework.stereotype.Service;
import org.vaadin.example.models.Department;
import org.vaadin.example.models.EmpDep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class EmpDepService {

    public ArrayList<EmpDep> getEmpDep() throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT " +
                "e.id as empId, e.name empName, d.id depId, d.name depName, j.id jobId, j.name jobName,e.phone as EmpPhone, e.email EmpEmail, d.email DepEmail, d.phone depPhone, j.salary JobSalary FROM employee as e " +
                "inner join intersection as i on e.id = i.employee_id " +
                "inner join department as d on i.department_id = d.id " +
                "inner join job as j on j.id =i.job_id";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<EmpDep> empDeps = new ArrayList<>();

        while (resultSet.next()) {
            int idEmp = resultSet.getInt("EMPID");
            String nameEmp = resultSet.getString("EMPNAME");
            int depId = resultSet.getInt("DEPID");
            String depName = resultSet.getString("DEPNAME");
            int jobId = resultSet.getInt("JOBID");
            String jobName = resultSet.getString("JOBNAME");
            String EmpPhone = resultSet.getString("EMPPHONE");
            String EmpEmail = resultSet.getString("EMPEMAIL");
            String DepEmail = resultSet.getString("DEPEMAIL");
            String depPhone = resultSet.getString("DEPPHONE");
            String JobSalary = resultSet.getString("JOBSALARY");
            empDeps.add(new EmpDep(
                    idEmp, nameEmp, depId, depName, jobId, jobName,EmpPhone,EmpEmail,DepEmail,depPhone,JobSalary
            ));
        }
        return empDeps;
    }
    /*public ArrayList<String> getEDJdet() throws SQLException{
        Connection conn = RedDatabaseConfig.getConnection();
        String query = "SELECT" +
                "e.phone as EmpPhone, e.email EmpEmail, d.email DepEmail, d.phone depPhone, j.salary JobSalary FROM employee as e " +
                "inner join intersection as i on e.id = i.employee_id " +
                "inner join department as d on i.department_id = d.id " +
                "inner join job as j on j.id =i.job_id";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> EDJdet = new ArrayList<>();
        while (resultSet.next()) {
            String EmpPhone = resultSet.getString("EMPPHONE");
            String EmpEmail = resultSet.getString("EMPEMAIL");
            String DepEmail = resultSet.getString("DEPEMAIL");
            String depPhone = resultSet.getString("DEPPHONE");
            String JobSalary = resultSet.getString("JOBSALARY");
            EDJdet.add(EmpPhone);   EDJdet.add(EmpEmail); EDJdet.add(DepEmail);
            EDJdet.add(depPhone); EDJdet.add(JobSalary);

        }
        return EDJdet;

    }*/

    public void addDepEmp(int idEmp, String jobName, ArrayList<String> depName) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String queryJob = "SELECT ID FROM JOB WHERE NAME = '"+jobName+"'";

        PreparedStatement statementJob = conn.prepareStatement(queryJob);
        ResultSet resultSetJob = statementJob.executeQuery();

        int idJob = -1;

        while (resultSetJob.next()) {
            idJob = resultSetJob.getInt("ID");
        }

        ArrayList<Integer> depsId = new ArrayList<>();

        depName.forEach(x ->{
                    String queryDep = "SELECT ID FROM DEPARTMENT WHERE NAME = '"+x+"'";
                    try {
                        PreparedStatement statement = conn.prepareStatement(queryDep);
                        ResultSet resultSetDep = statement.executeQuery();
                        while (resultSetDep.next()) {
                            depsId.add(resultSetDep.getInt("ID"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        int finalIdJob = idJob;
        depsId.forEach(x->{
            String query = "INSERT INTO INTERSECTION (employee_id, job_id, department_id) VALUES ("+
                    idEmp+","+ finalIdJob +","+x+")";
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String findJobEmp(int idEmp) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT name FROM intersection as i "+
        "inner join job as j on i.job_id = j.id where i.employee_id = " + idEmp+
                " rows 1";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        String name = "";

        while (resultSet.next()){
            name = resultSet.getString("NAME");
        }

        return name;
    }

    public ArrayList<String> findDep(int idEmp) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT name FROM intersection as i "+
                "inner join department as d on i.department_id = d.id where i.employee_id = " + idEmp;

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> names = new ArrayList<>();

        while (resultSet.next()){
            String name = resultSet.getString("NAME");
            names.add(name);
        }

        return names;
    }

    public void deleteInter(int idEmp) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "DELETE FROM intersection WHERE employee_id = " + idEmp;

        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
    }
}
