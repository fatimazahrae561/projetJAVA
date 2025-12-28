package com.jobs.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.jobs.database.DatabaseConnector;

public class CompetenceDAO {

    // Récupère toutes les compétences depuis la base
    public List<String> findAll() {
        List<String> competences = new ArrayList<>();
        String sql = "SELECT nom FROM competence";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                competences.add(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competences;
    }
}
