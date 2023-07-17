package org.vaadin.example;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;


import org.springframework.stereotype.Service;
import org.vaadin.example.models.Job;

@Service
public class JobService implements Serializable{

    public ArrayList<Job> getJobs() throws SQLException {

        Connection conn = RedDatabaseConfig.getConnection();
        String query = "SELECT * FROM JOB";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Job> jobs = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            float salary = resultSet.getFloat("SALARY");
            String name = resultSet.getString("NAME");

            jobs.add(new Job(id, name, salary));
        }

        return jobs;

    }

    public void addJob(String name, float salary) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String queryMaxId = "select max(id) from job";
        PreparedStatement statementMaxId = conn.prepareStatement(queryMaxId);
        ResultSet resultSetMaxId = statementMaxId.executeQuery();

        int maxId = -1;

        if(resultSetMaxId.next()){
            maxId = resultSetMaxId.getInt(1);
        }

        String queryInsert = "INSERT INTO JOB (ID, SALARY, NAME) VALUES ("+
                (maxId+1)+","+salary+","+"'"+name +"'" +")";

        PreparedStatement statementInsert = conn.prepareStatement(queryInsert);
        statementInsert.executeUpdate();
    }

    public void updateJob(int id, String name, float salary) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "UPDATE JOB SET NAME = "+
                "'"+name+"'"+", SALARY = "+salary+
                " WHERE ID = "+id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

    public void deleteJob(int id) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "DELETE FROM JOB WHERE id = "+id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

    public ArrayList<String> getJobName() throws SQLException{
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT NAME FROM JOB";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> jobs = new ArrayList<>();

        while (resultSet.next()) {
            String name = resultSet.getString("NAME");

            jobs.add(name);
        }

        return jobs;

    }
}
