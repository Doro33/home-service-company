package ir.maktab.homeservicecompany.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class AppContext {
    @Getter
    private static final  Random RANDOM = new Random();
    private AppContext() {
    }
}
