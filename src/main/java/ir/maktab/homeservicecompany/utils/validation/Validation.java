package ir.maktab.homeservicecompany.utils.validation;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class Validation {
    public void nameValidate(String firstName, String lastName){
        if (Strings.isNullOrEmpty(firstName))
            throw new IllegalArgumentException("first name cannot be null or empty.");
        if (Strings.isNullOrEmpty(lastName))
            throw new IllegalArgumentException("last name cannot be null or empty.");
    }

    public void passwordValidate(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$"))
            throw new IllegalArgumentException("""
                     password must contain at least 1 uppercase or lowercase and 1 digit.
                     password must have exactly 8 characters.
                    """);
    }

    public void imageValidate(MultipartFile image) {
        String[] choppedName = image.getName().split("\\.");
        int lastIndex = choppedName.length - 1;
        if (!Objects.equals(choppedName[lastIndex].toLowerCase(), "jpg")) {
            throw new IllegalArgumentException("image's format must be jpg.");
        }
        if (image.getSize() > 300_000L) {
            throw new IllegalArgumentException("image's size cannot be greater than 300kb.");
        }
    }
}
