package modelML.cleaning;

import java.io.*;

public class DatasetSentenceSplitter {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/jobs_clean.csv"), "UTF-8")
        );

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/main/resources/jobs_final.csv"), "UTF-8")
        );

        bw.write("job_description,skill");
        bw.newLine();
        String line;
        boolean header = true;

        while ((line = br.readLine()) != null) {

            if (header) {
                header = false;
                continue;
            }

            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (cols.length < 2) continue;

            String description = cols[0].replace("\"", "");
            String skill = cols[1].replace("\"", "");

            // Découpage en phrases
            String[] sentences = description.split("[\\.!?;]");

            for (String sentence : sentences) {
                sentence = sentence.trim();

                // Filtrage ML
                if (sentence.length() < 20 || sentence.length() > 200) continue;

                bw.write("\"" + sentence + "\",\"" + skill + "\"");
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println("Dataset FINAL prêt pour l'entraînement !");
    }
}
