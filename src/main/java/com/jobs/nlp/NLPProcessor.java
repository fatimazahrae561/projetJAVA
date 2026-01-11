package com.jobs.nlp;
import com.jobs.nlp.NLPProcessor;
import com.jobs.scraper.RekruteScraper;
import com.jobs.scraper.Scraper;
import com.jobs.service.OffreService;
import com.jobs.controller.DashboardController;
import com.jobs.dao.CompetenceDAO;
import com.jobs.dao.OffreDAO;

import java.text.Normalizer;
import com.jobs.dao.VilleDAO;
import com.jobs.model.Offre;
import com.jobs.model.ScrapedData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class NLPProcessor {
	
	private List<String> villes;       // villes récupérées depuis la base
    private List<String> competences;  // compétences récupérées depuis la base

    // ======= Constructeur =======
    public NLPProcessor() throws Exception {
        VilleDAO villeDAO = new VilleDAO();
        CompetenceDAO competenceDAO = new CompetenceDAO();

        this.villes = villeDAO.findAll();           // récupère toutes les villes
        this.competences = competenceDAO.findAll(); // récupère toutes les compétences
    }
	// ===================== ENTREPRISE =====================
	public String extractCompany(String text){
	    if(text == null) return "";

	    Pattern p = Pattern.compile("^\\s*(.+?)\\s+(recrute|recherche|cherche)",
	            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	    Matcher m = p.matcher(text);

	    if(m.find())
	        return m.group(1).trim();
	    
	    Pattern p2 = Pattern.compile("Entreprise\\s*:\\s*([^\\n]+)",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m2 = p2.matcher(text);
		if(m2.find())
		return m2.group(1).trim();
		

	    
	    return "";
	}


    // ===================== VILLE =====================
	public String extractCity(String text){
	    if(text == null) return "";

	    String normalizedText = normalize(text);

	    for(String v : villes){
	        if(normalizedText.contains(normalize(v)))
	            return v; // retourne la forme originale depuis ta liste
	    }

	    return "";
	}

	// fonction utilitaire pour retirer les accents et mettre en minuscule
	private String normalize(String value){
	    if(value == null) return "";
	    String norm = Normalizer.normalize(value, Normalizer.Form.NFD);
	    return norm.replaceAll("\\p{M}", "").toLowerCase();
	}

    // ===================== SECTEUR =====================
    public String extractSector(String text){
        if(text == null) return "";

        Pattern p = Pattern.compile(
            "Secteur d'activité\\s*:\\s*([^\\n\\r]+?)(?=Fonction|Expérience|Niveau|Type|$)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Matcher m = p.matcher(text);

        if(m.find()){
            String secteur = m.group(1).trim();
            secteur = secteur.replaceAll("\\s+", " "); // Nettoyage espaces
            return secteur;
        }

        return "";
    }




    // ===================== EXPERIENCE =====================
    public String extractExperience(String text){
        if(text == null) return "";

        // Chercher la ligne "Expérience requise : ..."
        Pattern p = Pattern.compile("Expérience requise\\s*:\\s*([^\\n]+)",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        Matcher m = p.matcher(text);

        if(m.find()){
            String expText = m.group(1).trim();

            // On gère d'abord les mentions textuelles
            String expLower = expText.toLowerCase();

            if(expLower.contains("débutant") && expLower.contains("junior")) {
                return "Débutant / Junior";
            }
            if(expLower.contains("débutant")){
                return "Débutant";
            }
            
            if(expLower.contains("junior")) {
                return "Junior";
            }
            if(expLower.contains("intermédiaire") && expLower.contains("confirmé")) {
                return "confirmé / intermédiaire";
            }
            
            if(expLower.contains("intermédiaire") && expLower.contains("Junior")) {
                return "Junior / intermédiaire";
            }
            if(expLower.contains("Expert") && expLower.contains("Confirmé")) {
                return "Confirmé / Expert";
            }
            
            if(expLower.contains("Expert")) {
                return "Expert";
            }
            
            if(expLower.contains("intermédiaire")) {
                return "Intermédiaire";
            }
            if(expLower.contains("confirmé")) {
                return "Confirmé";
            }
            if(expLower.contains("senior")) {
                return "Senior";
            }

         // 2️ Nouveau cas : "Niveau d'expérience : …"
            Pattern p2 = Pattern.compile("Niveau d'expérience\\s*:\\s*([^\\n]+)",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher m2 = p2.matcher(text);
            if (m2.find()) {
                return m2.group(1).trim();
            }

            // Si c'est un range numérique (ex : "De 3 à 5 ans", "1 à 3 ans")
            /*Pattern numPattern = Pattern.compile("(\\d+)");
            Matcher numMatch = numPattern.matcher(expText);
            List<Integer> values = new ArrayList<>();

            while(numMatch.find()){
                values.add(Integer.parseInt(numMatch.group(1)));
            }

            if(!values.isEmpty()){
                int min = Collections.min(values);
                int max = Collections.max(values);
                return min + " à " + max + " ans";
            }

            // Cas générique si aucune info détectée
            return expText; */
        }

        return "Non spécifiée";
    }



    // ===================== COMPETENCES =====================
    public List<String> extractSkills(String text){
        List<String> skills = new ArrayList<>();
        if(text == null || text.isEmpty()) return skills;

        // 1️⃣ Chercher "Compétences clés : ..."
        Pattern p = Pattern.compile(
        		"Compétences clés\\s*:\\s*([^0-9\\n]+)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
       

        Matcher m = p.matcher(text);

        if(m.find()){
            // 🔹 CAS 1 : Compétences clés trouvées
            String compText = m.group(1).trim();

            // Séparation par "-" ou ","
            String[] comps = compText.split("\\s*-\\s*|\\s*,\\s*");

            for(String c : comps){
                if(!c.isEmpty()){
                    skills.add(c.trim());
                }
            }

            return skills; // ⛔ on s'arrête ici
        }

        // 2️⃣ CAS 2 : fallback → recherche classique
        for(String skill : competences){
            if(text.toLowerCase().contains(skill.toLowerCase())){
                skills.add(skill);
            }
        }

        return skills;
    }


    
    
    private static String convertMonth(String month) {
        switch (month.toLowerCase()) {
            case "jan": return "01";
            case "feb": return "02";
            case "mar": return "03";
            case "apr": return "04";
            case "may": return "05";
            case "jun": return "06";
            case "jul": return "07";
            case "aug": return "08";
            case "sep": return "09";
            case "oct": return "10";
            case "nov": return "11";
            case "dec": return "12";
            default: return "00";
        }
    }

 // ===================== DATE DE PUBLICATION =====================
    public static String extractDate(String text) {
        if (text == null || text.isEmpty()) return "";

       
        Pattern pRange = Pattern.compile(
            "Publication\\s*:\\s*du\\s*(\\d{2}/\\d{2}/\\d{4})\\s*au\\s*(\\d{2}/\\d{2}/\\d{4})"
        );
        Matcher mRange = pRange.matcher(text);
        if (mRange.find()) {
            return mRange.group(1);
        }

        // 2️ Cas : date simple numerique
        Pattern pSingle = Pattern.compile("(\\d{2}[./]\\d{2}[./]\\d{4})");
        Matcher mSingle = pSingle.matcher(text);
        if (mSingle.find()) {
            return mSingle.group(1).replace('.', '/');
        }

        // 3️ Cas : Publie le: 
        Pattern pTextMonth = Pattern.compile(
            "Publiée\\s*le\\s*:\\s*(\\d{1,2})\\s*([A-Za-z]{3})",
            Pattern.CASE_INSENSITIVE
        );
        Matcher mTextMonth = pTextMonth.matcher(text);
        if (mTextMonth.find()) {
            String day = mTextMonth.group(1);
            String month = convertMonth(mTextMonth.group(2));

            // 
            String year = String.valueOf(java.time.Year.now().getValue());

            return String.format("%02d/%s/%s", Integer.parseInt(day), month, year);
        }

        return "";
    }



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {            
            NLPProcessor nlp = new NLPProcessor();
            OffreDAO dao = new OffreDAO();
            List<ScrapedData> data=dao.findAllOffreBrutes();
          
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
                System.out.println("annee : " + nlp.extractDate(description));

            
            }
            System.out.println("size: " + data.size());
            dao.saveAllToJobsTable(data);

           
            
    }catch (Exception e) {
            System.err.println("Une erreur s'est produite pendant l'exécution du scraper : " + e.getMessage());
            e.printStackTrace();
        }
    
}

		 
	}




