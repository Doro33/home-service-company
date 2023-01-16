package ir.maktab.homeservicecompany.utils;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class AppContext {
    @Getter
    private static final  Random RANDOM = new Random();
    private AppContext() {
    }
}
