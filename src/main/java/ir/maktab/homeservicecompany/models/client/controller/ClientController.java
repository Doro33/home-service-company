package ir.maktab.homeservicecompany.models.client.controller;

import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.comment.dto.CommentDTO;
import ir.maktab.homeservicecompany.models.comment.service.CommentService;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientSer;
    private final RequestService requestSer;
    private final OfferService offerSer;

    private final CommentService commentSer;

    @PutMapping("/changePassword")
    public String changePassword(@RequestBody PasswordDTO passwordDTO) {
        clientSer.changePassword(passwordDTO);
        return "your password updated successfully.";
    }

    @PostMapping("/addRequest")
    public String addRequest(@RequestBody RequestDTO requestDTO){
        requestSer.saveNewRequest(requestDTO);
        return "your request added successfully.";
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
    public String chooseAnOffer(@RequestBody ChooseOfferDTO chooseOfferDTO){
        clientSer.chooseAnOffer(chooseOfferDTO);
        return """
                offer chose successfully.
                the worker is on the road.
                """;
    }

    @PutMapping("/setRequestStatusOnStarted/{clientId}/{requestId}")
    public String setRequestStatusOnStarted(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.setRequestStatusOnStarted(clientId,requestId);
        return "request started successfully";
    }

    @PutMapping("/setRequestStatusOnCompleted/{clientId}/{requestId}")
    public String setRequestStatusOnCompleted(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.setRequestStatusOnCompleted(clientId,requestId);
        return "request completed successfully";
    }

    @PutMapping("/payWithCredit/{clientId}/{requestId}")
    public String payWithCredit(@PathVariable Long clientId,@PathVariable Long requestId){
        clientSer.payWithCredit(clientId,requestId);
        return "request paid successfully";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestBody CommentDTO commentDTO){
        commentSer.addComment(commentDTO);
        return "comment added successfully";
    }

    @PutMapping("/increaseCredit/{clientId}")
    public String increaseCredit(@PathVariable Long clientId,@RequestBody MoneyTransferDTO moneyTransferDTO){
        clientSer.increaseCredit(clientId,moneyTransferDTO);
        return "your credit increased successfully";
    }

    @GetMapping("/showCredit/{clientId}")
    public Double showCredit(@PathVariable Long clientId){
        return clientSer.findById(clientId).getCredit();
    }

    @GetMapping("/showMyRequests/{clientId}")
    public List<Request> showClientRequests(@PathVariable Long clientId){
        return requestSer.findByClient(clientId);
    }

    @PutMapping("/activateAccount/{clientId}")
    public String activateAccount(@PathVariable Long clientId){
        clientSer.activeClient(clientId);
        return """
                your account is active now.
                you are able to use all our services.
                hope you enjoy.
                """;
    }

}
