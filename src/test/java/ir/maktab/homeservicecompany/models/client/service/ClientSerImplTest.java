package ir.maktab.homeservicecompany.models.client.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({})
class ClientSerImplTest {
    @Autowired
    private ClientService clientSer;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByEmail() {
        assertNotNull(clientSer.findByEmail("f.afshar0@gmail.com"));
    }

    @Test
    void signUp() {
        Client client = new Client("farzad", "afshar", "f.afshar0@gmail.com", "1234567a");
        clientSer.signUp(client);
        assertNotNull(clientSer.findByEmail(client.getEmail()));
    }

    @Test
    void signUpIdIssue() {
        Client client = new Client("farzad", "afshar", "f.afshar@gmail.com", "1234567a");
        client.setId(0L);
        try {
            clientSer.signUp(client);
        } catch (InvalidIdException e) {
            assertEquals("new client's id must be null.", e.getMessage());
        }
    }

    @Test
    void signUpFirstNameIssue() {
        Client client = new Client("", "afshar", "f.afshar@gmail.com", "1234567a");
        try {
            clientSer.signUp(client);
        } catch (IllegalArgumentException e) {
            assertEquals("first name cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    void signUpLastNameIssue() {
        Client client = new Client("farzad", "", "f.afshar@gmail.com", "1234567a");
        try {
            clientSer.signUp(client);
        } catch (IllegalArgumentException e) {
            assertEquals("last name cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    void signUpEmailDuplicate() {
        Client client = new Client("farzad", "afshar", "f.afshar@gmail.com", "1234567a");
        try {
            clientSer.signUp(client);
        } catch (IllegalArgumentException e) {
            assertEquals("this email has been used.", e.getMessage());
        }
    }

    @Test
    void signUpPasswordIssue() {
        Client client = new Client("farzad", "afshar", "f.afshar1@gmail.com", "1");
        try {
            clientSer.signUp(client);
        } catch (IllegalArgumentException e) {
            assertEquals("""
                     password must contain at least 1 uppercase or lowercase and 1 digit.
                     password must have exactly 8 characters.
                    """, e.getMessage());
        }
    }

    @Test
    void changePassword() {
        Client client = new Client("milad","ameri","m.ameri@gmail.com","1234567a");
        clientSer.signUp(client);
        clientSer.changePassword(
                "m.ameri@gmail.com","1234567a","7654321a","7654321a");
        assertEquals("7654321a",clientSer.findByEmail("m.ameri@gmail.com").getPassword());
    }

    @Test
    void changePasswordEmailIssue(){
        try {
            clientSer.changePassword
                    ("0.@msn.com","1234567a","7654321a","76543321a");

        }catch (IllegalArgumentException e){
            assertEquals("this email does not have an account.",e.getMessage());
        }
    }

    @Test
    void changePasswordOldPasswordIssue(){
        Client client = new Client("milad","ameri","m.ameri1@gmail.com","1234567a");
        clientSer.signUp(client);
        try {
            clientSer.changePassword
                    ("m.ameri1@gmail.com","0123456a","7654321a","7654321a");
        }catch (IllegalArgumentException e){
            assertEquals("incorrect password",e.getMessage());
        }
    }

    @Test
    void changePasswordNewPasswordsIssue(){
        Client client = new Client("milad","ameri","m.ameri2@gmail.com","1234567a");
        clientSer.signUp(client);
        try {
            clientSer.changePassword
                    ("m.ameri2@gmail.com","1234567a","a11234567","7654321a");
        }catch (IllegalArgumentException e){
            assertEquals("new passwords are not match.",e.getMessage());
        }
    }

    @Test
    void changePasswordNewPasswordFormatIssue(){
        Client client = new Client("milad","ameri","m.ameri3@gmail.com","1234567a");
        clientSer.signUp(client);
        try {
            clientSer.changePassword
                    ("m.ameri3@gmail.com","1234567a","12345678","12345678");
        }catch (IllegalArgumentException e){
            assertEquals("""
                     password must contain at least 1 uppercase or lowercase and 1 digit.
                     password must have exactly 8 characters.
                    """,e.getMessage());
        }
    }

    @Test
    void addRequest() {
    }

    @Test
    void findOfferByRequestOrderByPrice() {
    }

    @Test
    void findOfferByRequestOrderByScore() {
    }

    @Test
    void setRequestStatusOnStarted() {
    }

    @Test
    void setRequestStatusOnCompleted() {
    }

    @Test
    void chooseAnOffer() {
    }

    @Test
    void payWithCredit() {
    }

    @Test
    void clientCriteria() {
    }
}