package com.jobs.dao;

import com.jobs.model.ScrapedData;
import com.jobs.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OffreDAO {

    public void saveAll(List<ScrapedData> offres) {
        String sql = "INSERT INTO offre (title, description, htmlBrut, url) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ScrapedData data : offres) {
                stmt.setString(1, data.getTitle());
                stmt.setString(2, data.getDescription());
                stmt.setString(3, data.getHtmlBrut());
                stmt.setString(4, data.getUrl());
                stmt.addBatch(); // ajouter à batch pour exécuter en lot
            }

            int[] result = stmt.executeBatch();
            System.out.println("Nombre d'offres insérées : " + result.length);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
