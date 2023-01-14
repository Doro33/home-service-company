package ir.maktab.homeservicecompany.models.client.dto;

import lombok.Getter;

@Getter
public class ClientFilterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Integer minRequestNumber;
    private Integer maxRequestNumber;

}
