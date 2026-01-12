package modelML.cleaning;

import java.io.*;

public class DatasetCleaner {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("src/main/resources/jobs_expanded.csv"), "UTF-8")
        );

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("src/main/resources/jobs_clean.csv"), "UTF-8")
        );

        String line;
        boolean header = true;

        while ((line = br.readLine()) != null) {

            if (header) {
                bw.write("job_description,skill");
                bw.newLine();
                header = false;
                continue;
            }

            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (cols.length < 2) continue;

            String description = cleanText(cols[0]);
            String skill = cleanText(cols[1]);

            bw.write("\"" + description + "\",\"" + skill + "\"");
            bw.newLine();
        }

        br.close();
        bw.close();

        System.out.println("Dataset propre prêt pour l'entraînement !");
    }

    private static String cleanText(String text) {
        return text
                .replaceAll("Â|â€™|â€|â€“|â€œ|â€�", "")
                .replaceAll("[^a-zA-Z0-9 ,.]", "")
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .trim();
    }
}
