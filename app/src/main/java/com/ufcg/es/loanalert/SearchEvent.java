package com.ufcg.es.loanalert;

public class SearchEvent {

    private final String queryString;

    public SearchEvent(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }

}
