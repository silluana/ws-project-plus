package com.client.ws.projectplus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTypeDto {

    private Long id;

    @NotBlank(message = "valor não pode ser nulo ou vazio")
    private String name;

    @NotBlank(message = "valor não pode ser nulo ou vazio")
    private String description;
}
