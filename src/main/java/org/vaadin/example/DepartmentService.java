package org.vaadin.example;

import org.springframework.stereotype.Service;
import org.vaadin.example.RedDatabaseConfig;
import org.vaadin.example.models.Department;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class DepartmentService implements Serializable {

    public ArrayList<Department> getDepartments() throws SQLException {

        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT * FROM DEPARTMENT";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Department> departments = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String email = resultSet.getString("EMAIL");
            String phone = resultSet.getString("PHONE");

            departments.add(new Department(id, name, email, phone));
        }
        return departments;
    }

    public void addDepartment(String name, String email, String phone) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();
        String queryMaxId = "select max(id) from DEPARTMENT";

        PreparedStatement statementMaxId = conn.prepareStatement(queryMaxId);
        ResultSet resultSetMaxId = statementMaxId.executeQuery();

        int maxId = -1;

        if(resultSetMaxId.next()){
            maxId = resultSetMaxId.getInt(1);
        }

        String queryInsert = "INSERT INTO DEPARTMENT (ID, NAME, EMAIL, PHONE) VALUES ("+
                (maxId+1)+",'"+name+"',"+"'"+email +"'" +",'"+phone+"')";

        PreparedStatement statementInsert = conn.prepareStatement(queryInsert);
        statementInsert.executeUpdate();
    }

    public void updateDepartment(int id, String name, String email, String phone) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "UPDATE DEPARTMENT SET NAME = '" +
                name+"', EMAIL = '" + email + "', PHONE = '" +
                phone + "' WHERE ID = " + id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

    public void deleteJob(int id) throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "DELETE FROM DEPARTMENT WHERE ID = "+id;

        PreparedStatement statementInsert = conn.prepareStatement(query);
        statementInsert.executeUpdate();
    }

    public ArrayList<String> getDepName() throws SQLException {
        Connection conn = RedDatabaseConfig.getConnection();

        String query = "SELECT NAME FROM DEPARTMENT";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> departments = new ArrayList<>();

        while (resultSet.next()) {
            String name = resultSet.getString("NAME");

            departments.add(name);
        }

        return departments;
    }

}
