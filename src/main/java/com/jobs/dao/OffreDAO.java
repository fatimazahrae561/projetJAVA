package com.jobs.dao;
import  com.jobs.nlp.NLPProcessor;
import com.jobs.model.Offre;
import com.jobs.model.ScrapedData;
import com.jobs.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
                String titre = data.getTitle();

                stmt.setString(1, data.getTitle());
                stmt.setString(2, nlp.extractCompany(description));
                stmt.setString(3, nlp.extractCity(titre+description));
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

    
    public List<Offre> findAll() {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT o.id, o.entreprise, o.ville,o.titre, o.secteur, o.experience, o.url, o.description, o.date_insertion, o.competences " +
                     "FROM offres_jobs o";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setEntreprise(rs.getString("entreprise"));

                // Pour ville et secteur on peut juste stocker le nom pour simplifier
                offre.setVille(rs.getString("ville"));
                offre.setSecteur(rs.getString("secteur"));

                offre.setExperience(rs.getString("experience"));
                offre.setUrlSource(rs.getString("url"));
                offre.setDescription(rs.getString("description"));
                offre.setTitle(rs.getString("titre"));

                offre.setDatePublication(rs.getDate("date_insertion"));
                
                // Compétences stockées en DB sous forme "Java,SQL,Spring"
                String competencesStr = rs.getString("competences");
                List<String> competencesList = new ArrayList<>();
                if (competencesStr != null && !competencesStr.isEmpty()) {
                    String[] comps = competencesStr.split(",");
                    for (String c : comps) {
                        competencesList.add(c.trim());
                    }
                }
                offre.setCompetences(competencesList);

                offres.add(offre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }

 // Filtrer par ville
    public List<Offre> findByVille(String ville) {
        List<Offre> offres = new ArrayList<>();

        String sql = "SELECT o.id, o.entreprise, o.ville, o.titre, o.secteur, o.experience, " +
                     "o.url, o.description, o.date_insertion, o.competences " +
                     "FROM offres_jobs o WHERE LOWER(o.ville) = LOWER(?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ville);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setEntreprise(rs.getString("entreprise"));
                offre.setVille(rs.getString("ville"));
                offre.setSecteur(rs.getString("secteur"));
                offre.setExperience(rs.getString("experience"));
                offre.setUrlSource(rs.getString("url"));
                offre.setDescription(rs.getString("description"));
                offre.setTitle(rs.getString("titre"));
                offre.setDatePublication(rs.getDate("date_insertion"));

                // compétences "Java, SQL, Spring"
                String competencesStr = rs.getString("competences");
                List<String> competencesList = new ArrayList<>();

                if (competencesStr != null && !competencesStr.isEmpty()) {
                    String[] comps = competencesStr.split(",");
                    for (String c : comps) competencesList.add(c.trim());
                }

                offre.setCompetences(competencesList);
                offres.add(offre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }
    
    
    public List<Offre> findBySecteur(String secteur) {
        List<Offre> offres = new ArrayList<>();

        String sql = "SELECT o.id, o.entreprise, o.ville, o.titre, o.secteur, o.experience, " +
                     "o.url, o.description, o.date_insertion, o.competences " +
                     "FROM offres_jobs o WHERE LOWER(o.secteur) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + secteur + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setEntreprise(rs.getString("entreprise"));
                offre.setVille(rs.getString("ville"));
                offre.setSecteur(rs.getString("secteur"));
                offre.setExperience(rs.getString("experience"));
                offre.setUrlSource(rs.getString("url"));
                offre.setDescription(rs.getString("description"));
                offre.setTitle(rs.getString("titre"));
                offre.setDatePublication(rs.getDate("date_insertion"));

                // compétences "Java, SQL, Spring"
                String competencesStr = rs.getString("competences");
                List<String> competencesList = new ArrayList<>();

                if (competencesStr != null && !competencesStr.isEmpty()) {
                    String[] comps = competencesStr.split(",");
                    for (String c : comps) {
                        competencesList.add(c.trim());
                    }
                }

                offre.setCompetences(competencesList);
                offres.add(offre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }

    public List<Offre> findByCompetence(String competence) {
        List<Offre> offres = new ArrayList<>();

        String sql = "SELECT o.id, o.entreprise, o.ville, o.titre, o.secteur, o.experience, " +
                     "o.url, o.description, o.date_insertion, o.competences " +
                     "FROM offres_jobs o " +
                     "WHERE LOWER(o.competences) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + competence + "%");   // recherche partielle
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setEntreprise(rs.getString("entreprise"));
                offre.setVille(rs.getString("ville"));
                offre.setSecteur(rs.getString("secteur"));
                offre.setExperience(rs.getString("experience"));
                offre.setUrlSource(rs.getString("url"));
                offre.setDescription(rs.getString("description"));
                offre.setTitle(rs.getString("titre"));
                offre.setDatePublication(rs.getDate("date_insertion"));

                // Récupérer les compétences
                String competencesStr = rs.getString("competences");
                List<String> competencesList = new ArrayList<>();

                if (competencesStr != null && !competencesStr.isEmpty()) {
                    String[] comps = competencesStr.split(",");
                    for (String c : comps) {
                        competencesList.add(c.trim());
                    }
                }

                offre.setCompetences(competencesList);
                offres.add(offre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }

    public List<Offre> findBy(String ville, String secteur, String competence) {

        List<Offre> offres = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT id, entreprise, ville, titre, secteur, experience, url, description, date_insertion, competences " +
            "FROM offres_jobs WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (ville != null && !ville.isEmpty()) {
            sql.append(" AND LOWER(ville) = LOWER(?) ");
            params.add(ville);
        }

        if (secteur != null && !secteur.isEmpty()) {
            sql.append(" AND LOWER(secteur) LIKE LOWER(?) ");
            params.add("%" + secteur + "%");
        }

        if (competence != null && !competence.isEmpty()) {
            sql.append(" AND LOWER(competences) LIKE LOWER(?) ");
            params.add("%" + competence + "%");
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Remplir les paramètres dynamiques
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setEntreprise(rs.getString("entreprise"));
                offre.setVille(rs.getString("ville"));
                offre.setSecteur(rs.getString("secteur"));
                offre.setExperience(rs.getString("experience"));
                offre.setUrlSource(rs.getString("url"));
                offre.setDescription(rs.getString("description"));
                offre.setTitle(rs.getString("titre"));
                offre.setDatePublication(rs.getDate("date_insertion"));

                String competencesStr = rs.getString("competences");
                List<String> competencesList = new ArrayList<>();

                if (competencesStr != null && !competencesStr.isEmpty()) {
                    for (String c : competencesStr.split(",")) {
                        competencesList.add(c.trim());
                    }
                }

                offre.setCompetences(competencesList);
                offres.add(offre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }



}
