package com.microprofile.payload;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema
public class ResultOfArray<T> {
    @Schema(required = true)
    public String message = "ok";

    @Schema(required = true)
    public Integer status = Response.Status.OK.getStatusCode();

    public List<T> records;

    public ResultOfArray(List<T> list) {
        this.records = list;
    }

    public ResultOfArray<T> status(Response.Status status) {
        this.status = status.getStatusCode();
        return this;
    }

    public ResultOfArray<T> message(String message) {
        this.message = message;
        return this;
    }
}
