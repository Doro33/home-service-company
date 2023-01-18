package ir.maktab.homeservicecompany.utils.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail,String toUserType ,Long id){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sin.mhm@gmail.com");
        message.setTo(toEmail);
        message.setSubject("account activation");
        message.setText("localhost:8081/" + toUserType +"/activateAccount/"+id);

        mailSender.send(message);
    }
}
