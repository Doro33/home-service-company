package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WorkerSerImplTest {
    @Autowired
    private WorkerService workerSer;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByEmail() {
        assertNotNull(workerSer.findByEmail("sin.mhm@gmail.com"));
    }

    @Test
    void signUp() {
        FileInputStream image = null;
        try {
            try {
                image = new FileInputStream("sin.jpg");
            }catch (FileNotFoundException f){
                throw new RuntimeException("cannot find the file.");
            }
            Worker worker = new Worker("sina", "mahmoudi", "sin.mhm1@gmail.com", "1234567a",image.readAllBytes());
            workerSer.signUp(worker);
            image.close();
            assertNotNull(workerSer.findByEmail(worker.getEmail()));
        } catch (IOException e) {
            throw new RuntimeException("image cannot be save.");
        }
    }

    @Test
    void addOffer() {
    }

    @Test
    void workerCriteria() {
    }
}