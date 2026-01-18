package com.microprofile.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public enum E_ROLE {
    @Schema(description = "普通用户")
    USER("USER"),

    @Schema(description = "管理员")
    ADMIN("ADMIN");

    public final String label;

    private E_ROLE(String label) {
        this.label = label;
    }
}
