package ir.maktab.homeservicecompany.models.client.controller;

import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientSer;

    @PutMapping("/changePassword")
    void changePassword(@RequestBody PasswordDTO passwordDTO) {
        clientSer.changePassword(
                passwordDTO.getEmail(),
                passwordDTO.getOldPassword(),
                passwordDTO.getNewPassword1(),
                passwordDTO.getNewPassword2());
    }

    @PostMapping("/addRequest")
    void addRequest(@RequestBody RequestDTO requestDTO){
        clientSer.addRequest(requestDTO);
    }
}
