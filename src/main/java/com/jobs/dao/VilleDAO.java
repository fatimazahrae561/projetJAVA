package com.jobs.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.jobs.database.DatabaseConnector;

public class VilleDAO {

    // Récupère toutes les villes depuis la base
    public List<String> findAll() {
        List<String> villes = new ArrayList<>();
        String sql = "SELECT nom FROM villes";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                villes.add(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return villes;
    }
}
