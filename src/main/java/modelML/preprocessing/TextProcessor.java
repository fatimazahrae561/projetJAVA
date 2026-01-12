package modelML.preprocessing;

import java.util.*;
import java.util.regex.*;

import modelML.prediction.SkillPredictor;

public class TextProcessor {

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
        "le","la","les","un","une","des","de","du","et","en","à","pour","dans","sur","avec","ce","ces",
        "il","elle","ils","elles","on","nous","vous","je","tu","me","te","se",
        "the","a","an","of","in","on","and","for","to","with","is","are","this","that","by","from"
    ));

    public List<String> process(String description) {
        if(description == null) return new ArrayList<>();

        String text = description.toLowerCase();

        // Supprimer URLs, dates et publications
        text = text.replaceAll("http\\S+|www\\.\\S+", "");
        text = text.replaceAll("publication:.*|posted on.*", "");
        // Supprimer tout sauf lettres, chiffres, / + - et espaces
        text = text.replaceAll("[^a-z0-9/\\+\\-\\s]", " ");
        text = text.replaceAll("\\s+", " ").trim();

        String[] tokens = text.split("\\s+");
        List<String> processed = new ArrayList<>();

        for(String token : tokens) {
            if(token.length() > 1 && !STOPWORDS.contains(token)) {
                processed.add(stem(token));
            }
        }

        return processed;
    }

    private String stem(String word) {
        // Ne pas tronquer certaines compétences importantes
        if(word.equalsIgnoreCase("powershell") ||
           word.equalsIgnoreCase("vbscript") ||
           word.equalsIgnoreCase("ci/cd") ||
           word.equalsIgnoreCase("docker") ||
           word.equalsIgnoreCase("kubernetes") ||
           word.equalsIgnoreCase("aws")) {
            return word;
        }

        // Tronquer les suffixes génériques
        /*if(word.endsWith("ing") || word.endsWith("é") || word.endsWith("er") || word.endsWith("ment")) {
            return word.substring(0, word.length()-3);  // ← ici, tu coupes 3 caractères systématiquement
        }*/
        return word;
    }
    public static void main(String[] args) throws Exception {
        
    }

}
