package test;

import com.jobs.model.ScrapedData;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EmploiMaScraperTest {

    @Test
    void testScrape() {
        // Création du scraper
        Scraper scraper = new EmploiMaScraper();

        // Appel de la méthode
        List<ScrapedData> data = scraper.scrape();

        // Vérifications basiques
        assertNotNull(data, "La liste de résultats ne doit pas être nulle");
        assertTrue(data.size() > 0, "La liste de résultats doit contenir au moins une offre");

        // Vérifier que chaque élément contient au moins un titre et une URL
        for (ScrapedData d : data) {
            assertNotNull(d.getTitle(), "Chaque offre doit avoir un titre");
            assertFalse(d.getTitle().isEmpty(), "Le titre ne doit pas être vide");
            assertNotNull(d.getUrl(), "Chaque offre doit avoir un lien URL");
            assertFalse(d.getUrl().isEmpty(), "L'URL ne doit pas être vide");
        }

        // Optionnel : afficher le nombre d'offres récupérées
        System.out.println("Nombre d'offres récupérées : " + data.size());
    }
}
