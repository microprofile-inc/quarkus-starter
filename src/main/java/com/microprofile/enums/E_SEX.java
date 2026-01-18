package com.microprofile.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public enum E_SEX {
    @Schema(description = "女性")
    WOMAN("WOMAN"),

    @Schema(description = "男性")
    MAN("MAN");

    public final String label;

    private E_SEX(String label) {
        this.label = label;
    }
}
