package com.microprofile.utils;

import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * build ViolationReport struct
 */
public final class GenerateViolationReport {
    private final ViolationReport violation;

    public GenerateViolationReport(String title, Response.Status status, List<ViolationReport.Violation> violations) {
        this.violation = new ViolationReport(title, status, violations);
    }

    public ViolationReport build() {
        return this.violation;
    }
}