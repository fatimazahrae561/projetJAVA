package com.jobs.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jobs.database.DatabaseConnector;
import java.util.stream.Collectors;

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
    
    public Map<String, Integer> getStatistiques() {

        Map<String, Integer> stats = new HashMap<>();

        String sql = "SELECT competences FROM offres_jobs WHERE competences IS NOT NULL";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String comps = rs.getString("competences");

                if(comps == null || comps.isEmpty())
                    continue;

                String[] list = comps.split(",");

                for(String c : list){
                    String comp = c.trim();

                    if(!comp.isEmpty()) {
                        stats.put(comp, stats.getOrDefault(comp, 0) + 1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public List<String> getTopCompetences(int limit){

        Map<String,Integer> stats = getStatistiques();

        return stats.entrySet()
                .stream()
                .sorted((a,b) -> b.getValue() - a.getValue())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
