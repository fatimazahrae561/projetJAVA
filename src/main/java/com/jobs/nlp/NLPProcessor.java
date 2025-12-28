package com.jobs.nlp;

import com.jobs.dao.CompetenceDAO;
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

        for(String v : villes){
            if(text.toLowerCase().contains(v.toLowerCase()))
                return v;
        }

        return "";
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

        Pattern p = Pattern.compile("Expérience requise\\s*:\\s*([^\\n]+)",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        Matcher m = p.matcher(text);

        if(m.find()){
            String expText = m.group(1);

            // chercher tous les nombres
            Pattern numPattern = Pattern.compile("(\\d+)");
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


