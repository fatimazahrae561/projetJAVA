package modelML.training;

import java.io.Serializable;
import java.util.*;

public class NaiveBayesModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // skill -> (word -> count)
    private Map<String, Map<String, Integer>> wordCounts = new HashMap<>();

    // skill -> number of samples
    private Map<String, Integer> skillCounts = new HashMap<>();

    // skill -> total number of words
    private Map<String, Integer> totalWordsPerSkill = new HashMap<>();

    // vocabulary
    private Set<String> vocabulary = new HashSet<>();

    private int totalSamples = 0;

    // =========================
    // TRAINING
    // =========================
    public void train(List<String> words, String skill) {

        totalSamples++;
        skillCounts.put(skill, skillCounts.getOrDefault(skill, 0) + 1);

        wordCounts.putIfAbsent(skill, new HashMap<>());
        totalWordsPerSkill.putIfAbsent(skill, 0);

        for (String word : words) {
            word = word.toLowerCase();
            vocabulary.add(word);

            Map<String, Integer> wc = wordCounts.get(skill);
            wc.put(word, wc.getOrDefault(word, 0) + 1);

            totalWordsPerSkill.put(skill, totalWordsPerSkill.get(skill) + 1);
        }
    }

    // =========================
    // PROBABILITIES
    // =========================
    public Map<String, Double> predictProba(List<String> words) {

        Map<String, Double> logScores = new HashMap<>();

        for (String skill : skillCounts.keySet()) {

            double logProb = Math.log(
                    (double) skillCounts.get(skill) / totalSamples
            );

            for (String word : words) {

                int count = wordCounts.get(skill).getOrDefault(word, 0);
                double prob = (count + 1.0) /
                        (totalWordsPerSkill.get(skill) + vocabulary.size());

                logProb += Math.log(prob);
            }

            logScores.put(skill, logProb);
        }

        // Softmax
        double maxLog = Collections.max(logScores.values());
        double sumExp = 0.0;

        for (double v : logScores.values()) {
            sumExp += Math.exp(v - maxLog);
        }

        Map<String, Double> probs = new HashMap<>();
        for (String skill : logScores.keySet()) {
            probs.put(skill, Math.exp(logScores.get(skill) - maxLog) / sumExp);
        }

        return probs;
    }

    // =========================
    // FINAL CLASS
    // =========================
    public String predict(List<String> words) {
        return Collections.max(
                predictProba(words).entrySet(),
                Map.Entry.comparingByValue()
        ).getKey();
    }

    // =========================
    // TEST LOCAL
    // =========================
    public static void main(String[] args) {

        NaiveBayesModel model = new NaiveBayesModel();

        model.train(
                Arrays.asList("hr", "management", "policy", "employees"),
                "hr management"
        );

        model.train(
                Arrays.asList("recruitment", "hiring", "interview"),
                "talent acquisition"
        );

        List<String> test = Arrays.asList("hiring", "employees", "policy");

        System.out.println("Probabilités :");
        model.predictProba(test).forEach(
                (s, p) -> System.out.println(s + " -> " + p)
        );

        System.out.println("\nClasse prédite : " + model.predict(test));
    }
    
}
