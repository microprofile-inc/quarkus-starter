package com.microprofile.payload;

import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

public class RegisterBody {
//    @Size(min = 2, max = 32)
//    @NotBlank
//    @NotEmpty
//    @NotNull
//    @Schema(required = true, description = "user name")
//    public String username;

    @NotBlank
    @NotEmpty
    @NotNull
    @Schema(required = true, description = "email")
    @Email
    public String email;

    @NotBlank
    @NotEmpty
    @NotNull
    @Schema(required = true, description = "phone number")
    public String phoneNumber;

    @NotBlank
    @NotEmpty
    @NotNull
    @Schema(required = true, description = "code")
    public String code;

    @NotNull
    @Schema(required = true, description = "country code id")
    public UUID countryCodeId;

//    @Enumerated(EnumType.STRING)
//    @Schema(required = true, description = "sex")
//    public E_SEX sex;

    @Size(min = 8, max = 32)
    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$",
            message = "密码必须为8-32位，且至少包含一个字母和一个数字"
    )

    @Schema(required = true, description = "password")
    public String password;
}
