package ir.maktab.homeservicecompany.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
