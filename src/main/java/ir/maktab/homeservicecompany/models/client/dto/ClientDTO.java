package ir.maktab.homeservicecompany.models.client.dto;

import lombok.Getter;

@Getter
public class ClientDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Integer orderCounter;

}
