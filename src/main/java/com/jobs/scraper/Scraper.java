package com.jobs.scraper;

import com.jobs.model.ScrapedData;
import java.util.List;

public interface Scraper {
	List<ScrapedData> scrape();
}
