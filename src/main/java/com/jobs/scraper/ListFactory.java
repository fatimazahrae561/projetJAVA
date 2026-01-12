
package com.jobs.scraper;

import java.util.ArrayList;
import java.util.List;

public class ListFactory<T> {

    public List<T> create() {
        return new ArrayList<>();
    }
}
