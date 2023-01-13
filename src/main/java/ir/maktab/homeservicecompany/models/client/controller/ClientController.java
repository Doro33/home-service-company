package ir.maktab.homeservicecompany.models.client.controller;

import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.comment.dto.CommentDTO;
import ir.maktab.homeservicecompany.models.comment.service.CommentService;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientSer;
    private final RequestService requestSer;
    private final OfferService offerSer;

    private final CommentService commentSer;

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

    @GetMapping("/findOffersOrderByPrice/{requestId}")
    @ResponseBody
    public List<Offer> findByRequestOrderByExpectedPrice(@PathVariable Long requestId){
        return offerSer.findByRequestOrderByExpectedPrice(requestId);
    }

    @GetMapping("/findOffersOrderByWorkerScore/{requestId}")
    @ResponseBody
    public List<Offer> findByRequestOrderByWorkerScore(@PathVariable Long requestId){
        return offerSer.findByRequestOrderByWorkerScore(requestId);
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

    @PutMapping("/payWithCredit/{clientId}/{requestId}")
    public void payWithCredit(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.payWithCredit(clientId,requestId);
    }

    @PostMapping("/addComment")
    public void addComment(@RequestBody CommentDTO commentDTO){
        commentSer.addComment(commentDTO);
    }

}
