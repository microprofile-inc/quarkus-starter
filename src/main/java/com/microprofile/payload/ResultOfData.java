package com.microprofile.payload;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public class ResultOfData<T> {
    @Schema(required = true)
    public String message = "ok";

    @Schema(required = true)
    public Integer status = Response.Status.OK.getStatusCode();

    public T data;

    public ResultOfData(T data) {
        this.data = data;
    }

    public ResultOfData<T> status(Response.Status status) {
        this.status = status.getStatusCode();
        return this;
    }

    public ResultOfData<T> message(String message) {
        this.message = message;
        return this;
    }
}
