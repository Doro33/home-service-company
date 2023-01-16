package ir.maktab.homeservicecompany.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
}
