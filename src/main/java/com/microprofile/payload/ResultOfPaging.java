package com.microprofile.payload;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class ResultOfPaging<T> {
    public long currentPageNumber;
    public long lastPageNumber;
    public long pageSize;
    public long totalRecords;
    public String message = "ok";

    public Integer status = Response.Status.OK.getStatusCode();
    public List<T> records;

    public ResultOfPaging(PanacheQuery<T> panacheQuery, Page page) {
        PanacheQuery<T> query = panacheQuery.page(page.index - 1, page.size);
        currentPageNumber = page.index;
        lastPageNumber = query.pageCount();
        pageSize = page.size;
        totalRecords = panacheQuery.count();
        records = query.list();
    }

    public ResultOfPaging<T> code(Response.Status status) {
        this.status = status.getStatusCode();
        return this;
    }

    public ResultOfPaging<T> message(String message) {
        this.message = message;
        return this;
    }
}
