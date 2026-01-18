package com.microprofile.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class SendCodeBody {
    @NotNull()
    public UUID countryCodeId;

    @Size(min = 11, max = 11)
    @NotBlank()
    @NotNull()
    @NotEmpty()
    public String phoneNumber;
}
