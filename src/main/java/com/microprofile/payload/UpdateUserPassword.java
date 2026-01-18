package com.microprofile.payload;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
public class UpdateUserPassword {
    @Size(min = 6, max = 64)
    @Schema(required = true, description = "password")
    public String password;

    @Size(min = 4, max = 4)
    @Schema(required = true, description = "code")
    public String code;
}
