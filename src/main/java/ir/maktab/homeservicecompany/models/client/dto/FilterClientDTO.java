package ir.maktab.homeservicecompany.models.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FilterClientDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Integer minRequestNumber;
    private Integer maxRequestNumber;
    private LocalDate signupAfter;
    private LocalDate signupBefore;

}
