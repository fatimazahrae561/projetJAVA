package com.jobs.scraper;

import com.jobs.dao.OffreDAO;
import com.jobs.model.ScrapedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class EmploiMaScraper implements Scraper {

    @Override
    public List<ScrapedData> scrape() {
        int page = 0;
        int totalOffers = 0;
        List<ScrapedData> results = new ArrayList<>();

        while (page < 29) {
            try {
                String url = "https://www.emploi.ma/recherche-jobs-maroc?page=" + page;
               // String url = "https://www.emploi.ma/recherche-jobs-maroc/ingenieur?page=" + page;

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("Connection", "keep-alive")
                        .header("Upgrade-Insecure-Requests", "1")
                        .timeout(15000)
                        .get();

                Elements offers = doc.select("div.card-job");

                for (Element offer : offers) {
                    ScrapedData data = new ScrapedData();
                    
                    String title = offer.select("h3 a").attr("title");
                    //String title = offer.select("h3 a.title").text();
                    String link = offer.attr("data-href");
                    String description = offer.select("div.card-job-detail").text();

                    data.setTitle(title);
                    data.setDescription(description);
                    data.setUrl(link);
                    data.setHtmlBrut(offer.html());

                    results.add(data);
                }
                totalOffers += offers.size();
                page++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        return results;
    }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
    	Scraper scraper = new EmploiMaScraper();
        List<ScrapedData> data = scraper.scrape();
        OffreDAO dao = new OffreDAO();
        System.out.println("size: " + data.size());
        //dao.saveAll(data);
        for (ScrapedData d : data) {
            System.out.println("URL: " + d.getUrl());
            System.out.println("----------------------- " );
            //System.out.println("html: " + d.getHtmlBrut());
            System.out.println("----------------------- " );
            System.out.println("title: " + d.getTitle());
            System.out.println("----------------------- " );
            System.out.println("DESC: " + d.getDescription()); 
            System.out.println("----------------------- " );
        }
        
        //dao.saveAll(data);
	}
}