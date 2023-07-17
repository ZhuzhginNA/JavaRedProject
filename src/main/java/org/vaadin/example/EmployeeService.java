package org.vaadin.example;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;


import org.springframework.stereotype.Service;
import org.vaadin.example.models.Employee;

@Service
public class EmployeeService implements Serializable{

    public ArrayList<Employee> getEmployee() throws SQLException {

        Connection conn = RedDatabaseConfig.getConnection();
        String query = "SELECT * FROM EMPLOYEE";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Employee> employee = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String phone = resultSet.getString("PHONE");
            String email = resultSet.getString("EMAIL");

            employee.add(new Employee(id, name,phone,email));
        }

        return employee;

    }

    public int addEmployee(String name, String phone,String email ) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String queryMaxId = "select max(id) from Employee";
        PreparedStatement statementMaxId = conn.prepareStatement(queryMaxId);
        ResultSet resultSetMaxId = statementMaxId.executeQuery();

        int maxId = -1;

        if(resultSetMaxId.next()){
            maxId = resultSetMaxId.getInt(1);
        }

        String queryInsert = "INSERT INTO Employee (ID, NAME, PHONE,EMAIL) VALUES ("+
                (maxId+1)+","+"'"+name +"'"+","+"'"+phone +"'" +"," + "'"+email +"'" +") RETURNING ID";

        PreparedStatement statementInsert = conn.prepareStatement(queryInsert);

        ResultSet resultSet = statementInsert.executeQuery();

        int id = -1;

        while (resultSet.next()) {

            id = resultSet.getInt("ID");

        }

        return id;
    }

    public void updateEmployee(int id, String name, String phone,String email) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "UPDATE EMPLOYEE SET NAME = "+
                "'"+name+"'"+", PHONE = " + "'" +phone+ "'" + ", EMAIL=" + "'"+email+"'" +
                " WHERE ID = "+id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

    public void deleteEmployee(int id) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "DELETE FROM EMPLOYEE WHERE id = " + id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

}