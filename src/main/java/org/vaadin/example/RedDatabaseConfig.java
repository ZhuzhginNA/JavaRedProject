package org.vaadin.example;

import com.vaadin.flow.server.VaadinServlet;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class RedDatabaseConfig extends VaadinServlet {
    private static final String DB_URL = "jdbc:firebirdsql://localhost:3050/C:/TEST.FDB?encoding=WIN1250";
    private static final String DB_USERNAME = "SYSDBA";
    private static final String DB_PASSWORD = "root";

    private static Connection conn;


    public RedDatabaseConfig() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static Connection getConnection() {
        return conn;
    }
}
