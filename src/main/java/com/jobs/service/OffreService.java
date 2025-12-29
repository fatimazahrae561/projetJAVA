
package com.jobs.service;

import com.jobs.dao.OffreDAO;
import com.jobs.model.Offre;

import java.util.List;

public class OffreService {

    private OffreDAO dao = new OffreDAO();

    public List<Offre> getAllOffres() {
        return dao.findAll();
    }

    /*public List<Offre> filtrerParVille(String ville) {
        return dao.findByVille(ville);
    }

    public List<Offre> filtrerParSecteur(String secteur) {
        return dao.findBySecteur(secteur);
    }

    public List<Offre> filtrerParCompetence(String competence) {
        return dao.findByCompetence(competence);
    }

    // Filtrage combiné
    public List<Offre> filtrer(String ville, String secteur, String competence) {
        // Ici tu peux ajouter la logique combinée selon tes besoins
        // Exemple simple : filtrer par ville puis par secteur puis par compétence
        List<Offre> filtered = dao.findAll();

        if (ville != null && !ville.isEmpty()) {
            filtered.removeIf(o -> !o.getVille().equalsIgnoreCase(ville));
        }
        if (secteur != null && !secteur.isEmpty()) {
            filtered.removeIf(o -> !o.getSecteur().equalsIgnoreCase(secteur));
        }
        if (competence != null && !competence.isEmpty()) {
            filtered.removeIf(o -> !o.getCompetences().contains(competence));
        }

        return filtered;
    }*/
}
