package com.microprofile.payload;


import com.microprofile.enums.E_SEX;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
public class UpdateUser {
    @Size(min = 2, max = 32)
    @Schema(description = "username", required = true)
    public String username;

    @Size(min = 2, max = 32)
    @Schema(description = "url slug", required = true, minLength = 2, maxLength = 32)
    public String slug;

    @Size(min = 2, max = 128)
    @Schema(description = "website url", minLength = 2, maxLength = 128)
    public String website;

    @Enumerated(EnumType.STRING)
    @Schema(description = "sex")
    public E_SEX sex;
}
