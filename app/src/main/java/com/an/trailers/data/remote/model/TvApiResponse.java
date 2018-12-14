package com.an.trailers.data.remote.model;



import com.an.trailers.data.local.entity.TvEntity;

import java.util.List;

public class TvApiResponse {

    private long page;
    private long totalPages;
    private long totalResults;
    private List<TvEntity> results;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<TvEntity> getResults() {
        return results;
    }

    public void setResults(List<TvEntity> results) {
        this.results = results;
    }
}
