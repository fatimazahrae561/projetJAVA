package com.jobs.nlp;

import com.jobs.dao.CompetenceDAO;
import java.text.Normalizer;
import com.jobs.dao.VilleDAO;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;



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
        if(text == null) return skills;

        for(String skill : competences){
            if(text.toLowerCase().contains(skill.toLowerCase()))
                skills.add(skill);
        }

        return skills;
    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


