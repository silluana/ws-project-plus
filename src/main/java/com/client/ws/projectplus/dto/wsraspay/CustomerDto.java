package com.client.ws.projectplus.dto.wsraspay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String id;

    private String cpf;

    private String email;

    private String firstName;

    private String lastName;

}
