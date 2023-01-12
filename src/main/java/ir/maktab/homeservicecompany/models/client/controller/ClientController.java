package ir.maktab.homeservicecompany.models.client.controller;

import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientSer;
    private final RequestService requestSer;

    @PutMapping("/changePassword")
    public void changePassword(@RequestBody PasswordDTO passwordDTO) {
        clientSer.changePassword(
                passwordDTO.getEmail(),
                passwordDTO.getOldPassword(),
                passwordDTO.getNewPassword1(),
                passwordDTO.getNewPassword2());
    }

    @PostMapping("/addRequest")
    public void addRequest(@RequestBody RequestDTO requestDTO){
        requestSer.saveNewRequest(requestDTO);
    }

    @PostMapping("/chooseAnOffer")
    public void chooseAnOffer(@RequestBody ChooseOfferDTO chooseOfferDTO){
        clientSer.chooseAnOffer(chooseOfferDTO);
    }

    @PutMapping("/setRequestStatusOnStarted/{clientId}/{requestId}")
    public void setRequestStatusOnStarted(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.setRequestStatusOnStarted(clientId,requestId);
    }

    @PutMapping("/setRequestStatusOnCompleted/{clientId}/{requestId}")
    public void setRequestStatusOnCompleted(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.setRequestStatusOnCompleted(clientId,requestId);
    }

}
