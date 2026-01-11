package modelML;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatasetExpander {

    public static void main(String[] args) throws Exception {

        // Lecture depuis resources
        InputStream is = DatasetExpander.class
                .getClassLoader()
                .getResourceAsStream("all_job_post.csv");

        if (is == null) {
            System.out.println("Fichier CSV introuvable !");
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/main/resources/jobs_expanded.csv"),
                        StandardCharsets.UTF_8
                )
        );

        String line;
        boolean header = true;

        // ecrire en-tête
        bw.write("job_description,skill");
        bw.newLine();

        while ((line = br.readLine()) != null) {

            // ignorer la ligne d'en-tête originale
            if (header) {
                header = false;
                continue;
            }

            // Split CSV robuste (gere les virgules dans les textes)
            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            if (cols.length < 5) continue;

            String description = cols[3]
                    .replace("\"", "")
                    .replaceAll("\\s+", " ")
                    .trim();

            String skillsRaw = cols[4]
                    .replace("\"", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace("'", "")
                    .trim();

            if (skillsRaw.isEmpty()) continue;

            String[] skills = skillsRaw.split(",");

            for (String skill : skills) {
                if (skill.trim().isEmpty()) continue;

                bw.write("\"" + description + "\",\"" + skill.trim().toLowerCase() + "\"");
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println(" Dataset prêt pour l'entraînement ML !");
    }
}
