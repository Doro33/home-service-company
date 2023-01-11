package ir.maktab.homeservicecompany.utils.reCaptcha;

import lombok.Data;

@Data
public class ReCaptchaResponse {

    private Boolean success;
    private String challenge_ts;
    private String hostname;
}