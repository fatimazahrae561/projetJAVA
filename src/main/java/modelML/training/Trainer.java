package modelML.training;

import modelML.preprocessing.TextProcessor;

import java.io.*;
import java.util.*;

public class Trainer {

    public static void main(String[] args) throws Exception {

        String inputFile = "src/main/resources/jobs_final.csv";
        String modelFile = "src/main/resources/naive_bayes.model";

        NaiveBayesModel model = new NaiveBayesModel();
        TextProcessor processor = new TextProcessor();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), "UTF-8")
        );

        String line;
        boolean header = true;

        while ((line = br.readLine()) != null) {

            if (header) {
                header = false;
                continue;
            }

            // CSV safe split
            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (cols.length < 2) continue;

            String description = cols[0].replace("\"", "");
            String skill = cols[1].replace("\"", "");

            List<String> words = processor.process(description);

            if (!words.isEmpty()) {
                model.train(words, skill);
            }
        }

        br.close();

        // Sauvegarde du modèle
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(modelFile)
        );
        oos.writeObject(model);
        oos.close();

        System.out.println("✅ Modèle entraîné et sauvegardé !");
    }
}
