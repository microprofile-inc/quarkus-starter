package com.microprofile.payload;

import io.quarkus.panache.common.Page;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public class PageParams {
    private static final int miniSize = 2;
    private static final int maxSize = 40;

    @Schema
    @QueryParam("page")
    public int page;

    @Schema
    @QueryParam("pageSize")
    public int pageSize;

    public static Page of(PageParams pageParams) {
        int page = pageParams.page;
        int pageSize = pageParams.pageSize;
        if(pageParams.page < 1) {
            page = 1;
        }
        if(pageParams.pageSize < PageParams.miniSize || pageParams.pageSize > PageParams.maxSize) {
            pageSize = PageParams.miniSize;
        }
        return Page.of(page, pageSize);
    }
}
