package ir.maktab.homeservicecompany.models.client.dto;

import lombok.Getter;

@Getter
public class FilterClientDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Integer minRequestNumber;
    private Integer maxRequestNumber;

}
