package com.jobs.scraper;
import com.jobs.dao.OffreDAO;
import com.jobs.model.ScrapedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MarocAnnoncesScraper implements Scraper {

    @Override
    public List<ScrapedData> scrape() {
        int page = 0;
        List<ScrapedData> results = new ArrayList<>();

        while (page < 80) {
            try {
            	String listUrl = "https://www.marocannonces.com/maroc/offres-emploi-b309.html?pge="+ page ;
                System.out.println("Page: " + listUrl);

                Document doc = Jsoup.connect(listUrl)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();
                //System.out.println(doc.html());
                //Elements offers = doc.select("ul.cars-list li");
                Elements offers = doc.select("ul.cars-list > li:not(.adslistingpos)");

                for (Element offer : offers) {
                    
                    String title = offer.selectFirst("a").attr("title");
                    String href = offer.selectFirst("a").attr("href");
                    String detailUrl = "https://www.marocannonces.com/" + href;
                   // Element link = offer.selectFirst("h3 a");
                    //if (link == null) continue;
                    //String detailUrl = link.absUrl("href");


                    //  Scraper la page détail
                    Document detailDoc;
                    try {
                        detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0")
                                .timeout(15000)
                                .get();
                    } catch (Exception ex) {
                        System.out.println("URL ignorée (invalide): " + detailUrl);
                        continue; // IGNORER CETTE ANNONCE
                    }

                    Elements descElements = detailDoc.select("div.description div.block, ul.info-holder");
                    StringBuilder description = new StringBuilder();

                    for (Element el : descElements) {
                        description.append(el.text()).append("\n");
                    }

                    String finalDescription = description.toString().trim();


                    ScrapedData data = new ScrapedData();
                    data.setTitle(title);
                    data.setUrl(detailUrl);
                    data.setDescription(finalDescription);
                    data.setHtmlBrut(detailDoc.html());

                    results.add(data);
                    Thread.sleep(800); // très important

                }

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
    	Scraper scraper = new MarocAnnoncesScraper();
        List<ScrapedData> data = scraper.scrape();
        OffreDAO dao = new OffreDAO();
        System.out.println("test ");
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
        dao.saveAll(data);
    

    }
    
   
}

