package com.jobs.scraper;

import com.jobs.scraper.RekruteScraper;
import com.jobs.scraper.Scraper;
import com.jobs.service.OffreService;
import com.jobs.model.Offre;
import com.jobs.model.ScrapedData;
import com.jobs.dao.CompetenceDAO;
import com.jobs.dao.OffreDAO;
import com.jobs.nlp.NLPProcessor;
import java.util.List;
import com.jobs.controller.DashboardController;
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
            List<Offre> offresCasa = Offre();
            offresCasa=offreservice.filtrerParVille("Casablanca");
            
            List<Offre> offresInfo = Offre();
            offresInfo=offreservice.filtrerParSecteur("automobile");
            
            List<Offre> offresSql = Offre();
            offresSql=offreservice.filtrerParCompetence("Sql");
            
            List<Offre> offresfilrer = Offre();
            offresfilrer=offreservice.filtrerParCombinaison("rabat", "informatique", "sql");
            
            
            DashboardController dashboardController=new DashboardController();
            List<Offre> jobs =dashboardController.getOffers();
            
            /*
            CompetenceDAO stats = new CompetenceDAO();
            System.out.println("statistique: "+stats.getStatistiques());
            CompetenceDAO statsTOP5 = new CompetenceDAO();
            System.out.println("statistique: "+statsTOP5.getTopCompetences(20));
            
            
            for (ScrapedData d : data) {
            	String title = d.getTitle();
                String description = d.getDescription();
                System.out.println("----------------------------------------");
                System.out.println("title: " + d.getTitle());
                System.out.println("Entreprise : " + nlp.extractCompany(description));
                System.out.println("Ville : " + nlp.extractCity(title+description));
                System.out.println("Secteur : " + nlp.extractSector(description));
                System.out.println("Expérience : " + nlp.extractExperience(description));
                System.out.println("Compétences : " + nlp.extractSkills(description));
            }*/
            
            //dao.saveAllToJobsTable(data);

            /* dao.saveAll(data);
            for (ScrapedData d : data) {
                System.out.println("URL: " + d.getUrl());
                System.out.println("title: " + d.getTitle());
                System.out.println("DESC: " + d.getDescription());
                System.out.println("----------------------------");
            }*/
             
            System.out.println("Nombre d'offres récupérées : " + jobs.size());
            for (Offre o : jobs) {
                System.out.println(o.toString());}
           /*

            System.out.println("Nombre d'offres récupérées Casablanca: " + offresCasa.size());
            for (Offre o : offresCasa) {
                System.out.println(o.toString());}
            
           
            System.out.println("Nombre d'offres récupérées info: " + offresInfo.size());
            for (Offre o : offresInfo) {
                System.out.println(o.toString());}
          
            System.out.println("Nombre d'offres récupérées info: " + offresSql.size());
            for (Offre o : offresSql) {
                System.out.println(o.toString());}
        
            
            System.out.println("Nombre d'offres récupérées info: " + offresfilrer.size());
            for (Offre o : offresfilrer) {
                System.out.println(o.toString());}
            
      */       
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