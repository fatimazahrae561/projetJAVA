package modelML.cleaning;

import java.io.*;

public class DatasetExpander {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("src/main/resources/all_job_post.csv"), "UTF-8"));

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("src/main/resources/jobs_expanded.csv"), "UTF-8"));

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
            if (cols.length < 5) continue;

            String description = cols[3]
                    .replace("\"", "")
                    .replace("\n", " ")
                    .replace("\t", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

            String skillsRaw = cols[4]
                    .replace("\"", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace("'", "")
                    .toLowerCase()
                    .trim();

            // 🔴 Filtrage CRITIQUE
            if (description.length() < 80) continue;

            String[] skills = skillsRaw.split(",");

            for (String skill : skills) {
                skill = skill.trim();

                if (skill.length() < 2 || skill.length() > 40) continue;

                bw.write("\"" + description + "\",\"" + skill + "\"");
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println("Dataset nettoyé et cohérent ✔");
    }
}
