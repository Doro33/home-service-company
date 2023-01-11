package ir.maktab.homeservicecompany;

import ir.maktab.homeservicecompany.models.comment.entity.Comment;
import ir.maktab.homeservicecompany.models.request.entity.Request;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        LocalTime startAt = LocalTime.of(10,30);
        LocalTime endAt = LocalTime.of(14,30);

        Duration actualDuration = Duration.between(startAt,endAt);
        Duration expectedDuration = actualDuration.minus(6,ChronoUnit.HOURS);

        long extraHours = actualDuration.toHours()-expectedDuration.toHours();
        System.out.println(expectedDuration.toHours());

        double amount = 99;
        System.out.println(amount*0.7);

        Comment comment = Comment.builder(new Request(),2L).build();
        Comment comment2 = Comment.builder(new Request(),5L).description("mamad").build();
        System.out.println(comment);
        System.out.println(comment2);

    }
}
