package ir.maktab.homeservicecompany.models.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterClientDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Integer minRequestNumber;
    private Integer maxRequestNumber;

}
