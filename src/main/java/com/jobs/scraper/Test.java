package com.jobs.scraper;

import com.jobs.scraper.RekruteScraper;
import com.jobs.scraper.Scraper;
import com.jobs.service.OffreService;
import com.jobs.model.Offre;
import com.jobs.model.ScrapedData;
import com.jobs.dao.OffreDAO;
import com.jobs.nlp.NLPProcessor;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        try {
            Scraper scraper = new RekruteScraper();
            List<ScrapedData> data = scraper.scrape();
            OffreDAO dao = new OffreDAO();
            NLPProcessor nlp = new NLPProcessor();
            OffreService offreservice = new OffreService();
            List<Offre> offres = Offre();
            offres= offreservice.getAllOffres();
            
            

            /*for (ScrapedData d : data) {
                String description = d.getDescription();
                System.out.println("----------------------------------------");
                System.out.println("Entreprise : " + nlp.extractCompany(description));
                System.out.println("Ville : " + nlp.extractCity(description));
                System.out.println("Secteur : " + nlp.extractSector(description));
                System.out.println("Expérience : " + nlp.extractExperience(description));
                System.out.println("Compétences : " + nlp.extractSkills(description));
            }*/
            
           // dao.saveAllToJobsTable(data);

            // dao.saveAll(data);
            /*for (ScrapedData d : data) {
                System.out.println("URL: " + d.getUrl());
                System.out.println("title: " + d.getTitle());
                System.out.println("DESC: " + d.getDescription());
                System.out.println("----------------------------");
            }*/
            
            System.out.println("Nombre d'offres récupérées : " + offres.size());
            for (Offre o : offres) {
                System.out.println(o.toString());
        }
        }catch (Exception e) {
            System.err.println("Une erreur s'est produite pendant l'exécution du scraper : " + e.getMessage());
            e.printStackTrace();
        }
    
}

	private static List<Offre> Offre() {
		// TODO Auto-generated method stub
		return null;
	}
}