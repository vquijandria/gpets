package com.gpets.gpetsapi.dto;

import jakarta.validation.constraints.NotBlank;

public class OwnerCreateRequest {
    public String fullName;
    @NotBlank public String phone;
    @NotBlank public String address;
}
