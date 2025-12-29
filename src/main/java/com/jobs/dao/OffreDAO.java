package com.jobs.dao;
import  com.jobs.nlp.NLPProcessor;
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
   
    
    public void saveAllToJobsTable(List<ScrapedData> offres) {
        String sql = "INSERT INTO offres_jobs "
                   + "(titre, entreprise, ville, secteur, experience, competences, url, description, html_brut) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        NLPProcessor nlp;
		try {
			nlp = new NLPProcessor();
		
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ScrapedData data : offres) {
                String description = data.getDescription();

                stmt.setString(1, data.getTitle());
                stmt.setString(2, nlp.extractCompany(description));
                stmt.setString(3, nlp.extractCity(description));
                stmt.setString(4, nlp.extractSector(description));
                stmt.setString(5, nlp.extractExperience(description));
                stmt.setString(6, String.join(", ", nlp.extractSkills(description)));
                stmt.setString(7, data.getUrl());
                stmt.setString(8, description);
                stmt.setString(9, data.getHtmlBrut());

                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            System.out.println("Nombre d'offres insérées dans 'offres_jobs' : " + result.length);

        } catch (SQLException e) {
            e.printStackTrace();
        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    
    } 
    
}
