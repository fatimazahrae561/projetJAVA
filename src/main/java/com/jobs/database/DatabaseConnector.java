package com.jobs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/jobs_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // ton user
    private static final String PASSWORD = ""; // ton mot de passe

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
