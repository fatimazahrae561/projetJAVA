package com.jobs.controller;

import com.jobs.service.OffreService;
import com.jobs.dao.CompetenceDAO;
import com.jobs.model.Offre;

import java.util.List;
import java.util.Map;

public class DashboardController {

    private final OffreService offreService;
    private final CompetenceDAO competenceDAO;

    public DashboardController() {
        this.offreService = new OffreService();
        this.competenceDAO = new CompetenceDAO();
    }

    // ---- 1. Toutes les offres ----
    public List<Offre> getOffers() {
        return offreService.getAllOffres();
    }

    // ---- 2. Filtrer par ville ----
    public List<Offre> getByVille(String ville) {
        return offreService.filtrerParVille(ville);
    }

    // ---- 3. Filtrer par secteur ----
    public List<Offre> getBySecteur(String secteur) {
        return offreService.filtrerParSecteur(secteur);
    }

    // ---- 4. Filtrer par compétence ----
    public List<Offre> getByCompetence(String competence) {
        return offreService.filtrerParCompetence(competence);
    }

    // ---- 5. Filtre combiné ----
    public List<Offre> filterCombined(String ville, String secteur, String competence) {
        return offreService.filtrerParCombinaison(ville, secteur, competence);
    }

    // ---- 6. Top compétences ----
    public List<String> getTopCompetences(int limit) {
        return competenceDAO.getTopCompetences(limit);
    }

    // ---- 7. Statistiques compétences ----
    public Map<String, Integer> getCompetenceStats() {
        return competenceDAO.getStatistiques();
    }
}
