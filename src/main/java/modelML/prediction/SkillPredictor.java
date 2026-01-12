package modelML.prediction;

import modelML.preprocessing.TextProcessor;
import java.util.*;
import java.io.*;

public class SkillPredictor implements Serializable {

    private Map<String, Map<String, Integer>> wordCountsBySkill = new HashMap<>();
    private Map<String, Integer> skillCounts = new HashMap<>();
    private Set<String> vocabulary = new HashSet<>();
    private int totalDocs = 0;

    private TextProcessor processor = new TextProcessor();

    // Liste de toutes les compétences possibles (pour extraction exacte)
    private List<String> allSkills = Arrays.asList(
        "Java","Spring Boot","Docker","Kubernetes","CI/CD","VBScript",
        "PowerShell","Python","Linux","AWS","DevOps","HR","Gestion projet",
        "SQL","JavaScript","React","Angular","Node.js","Excel","Tableau","Git","Docker"
    );

    // ================= Entraînement =================
    public void train(List<String> descriptions, List<String> skills) {
        for(int i=0; i<descriptions.size(); i++) {
            String skill = skills.get(i);
            String desc = descriptions.get(i);

            skillCounts.put(skill, skillCounts.getOrDefault(skill, 0)+1);
            totalDocs++;

            List<String> tokens = processor.process(desc);
            vocabulary.addAll(tokens);

            Map<String, Integer> counts = wordCountsBySkill.getOrDefault(skill, new HashMap<>());
            for(String token : tokens) {
                counts.put(token, counts.getOrDefault(token,0)+1);
            }
            wordCountsBySkill.put(skill, counts);
        }

        // Ajouter toutes les compétences dans le vocabulaire pour être sûr de les détecter
        for(String skill : allSkills) {
            vocabulary.add(skill.toLowerCase());
        }
    }

    // ================= Prédiction principale =================
    public String predict(String description) {
        List<String> tokens = processor.process(description);
        double bestProb = Double.NEGATIVE_INFINITY;
        String bestSkill = null;

        for(String skill : skillCounts.keySet()) {
            double logProb = Math.log(skillCounts.get(skill)/(double)totalDocs);
            Map<String,Integer> counts = wordCountsBySkill.get(skill);
            int totalWordsInSkill = counts.values().stream().mapToInt(Integer::intValue).sum();

            for(String token : tokens) {
                int count = counts.getOrDefault(token,0);
                logProb += Math.log((count+1.0)/(totalWordsInSkill + vocabulary.size()));
            }

            if(logProb > bestProb) {
                bestProb = logProb;
                bestSkill = skill;
            }
        }

        return bestSkill != null ? bestSkill : "Unknown";
    }

    // ================= Extraction de toutes les compétences exactes =================
    public List<String> extractAllSkills(String description){
        List<String> detected = new ArrayList<>();
        String text = description.toLowerCase();

        for(String skill : allSkills){
            if(text.contains(skill.toLowerCase()) && !detected.contains(skill)){
                detected.add(skill);
            }
        }

        return detected;
    }

    // ================= Sauvegarde =================
    public void saveModel(String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(this);
        oos.close();
    }

    // ================= Chargement =================
    public static SkillPredictor loadModel(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        SkillPredictor model = (SkillPredictor) ois.readObject();
        ois.close();
        return model;
    }

    // ================= Test =================
    public static void main(String[] args) throws Exception {
        SkillPredictor model = new SkillPredictor();

        List<String> descriptions = Arrays.asList(
            "Gestion de projets DevOps sur AWS et Docker",
            "Recrutement, HR management et suivi des employés",
            "Développement Java et Spring Boot pour applications web"
        );
        List<String> skills = Arrays.asList("DevOps", "HR", "Java");

        model.train(descriptions, skills);

        String testDesc = "DXC Technology recherche un Ingénieur Poste de Travail Middle Senior " +
                "avec des compétences en VBScript et PowerShell, Docker et CI/CD.";
        
        System.out.println("Compétence principale : " + model.predict(testDesc));
        System.out.println("Compétences détectées : " + model.extractAllSkills(testDesc));
    }
}
