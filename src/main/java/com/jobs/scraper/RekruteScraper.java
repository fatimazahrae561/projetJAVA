package com.jobs.scraper;
import com.jobs.model.ScrapedData;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RekruteScraper implements Scraper {
	
    //private static final String URL = "https://www.rekrute.com/offres.html?s=3&p="+page+"&o=1";
    

    @Override
    public List<ScrapedData> scrape() {
        int page = 1;
        int totalOffers = 0; // variable pour compter toutes les offres
        List<ScrapedData> results = new ArrayList<>();

        while (page < 33) {
            try {
                String URL = "https://www.rekrute.com/offres.html?s=3&p=" + page + "&o=1";
                Document doc = Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                                + "AppleWebKit/537.36 (KHTML, like Gecko) "
                                + "Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept-Language", "fr-FR,fr;q=0.9")
                        .timeout(10000)
                        .get();

                Elements offers = doc.select("li.post-id");
                System.out.println("Nombre d'offres trouvées sur la page " + page + ": " + offers.size());

                for (Element offer : offers) {
                    ScrapedData data = new ScrapedData();

                    Element descriptiondiv = offer.selectFirst("div.holder");
                    String description = (descriptiondiv != null) ? descriptiondiv.text() : "";

                    Element titleEl = offer.selectFirst("h2");
                    String title = (titleEl != null) ? titleEl.text() : "";

                    Element linkEl = offer.selectFirst("a.titreJob");
                    String url = (linkEl != null) ? "https://www.rekrute.com" + linkEl.attr("href") : "";

                    data.setTitle(title);
                    data.setDescription(description);
                    data.setHtmlBrut(offer.html());
                    data.setUrl(url);

                    results.add(data);
                }

                totalOffers += offers.size(); // ajouter le nombre d'offres de cette page au total
                page++;

            } catch (Exception e) {
                e.printStackTrace();
                break; // sortir de la boucle si erreur
            }
        }

        System.out.println("Nombre total d'offres sur toutes les pages : " + totalOffers);
        return results;
    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
