package ir.maktab.homeservicecompany.models.worker.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@NotNull
public class Worker extends BaseEntity {
    public Worker(String firstName, String lastName, String email, String password, byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.credit = 0D;
        this.status = WorkerStatus.AWAITING_FOR_CONFIRM;
        this.image = image;
        this.score = 0L;
        this.commentCounter = 0;
        this.offerCounter = 0;
    }

    private String firstName;
    private String lastName;
    @Column(unique = true)
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$",
            message = """
                    password must contain at least 1 uppercase or lowercase and 1 digit.
                    password must have exactly 8 characters.
                    """)
    private String password;
    private LocalDateTime createdAt;
    private Double credit;
    @Enumerated(EnumType.STRING)
    private WorkerStatus status;
    @Lob
    @Nullable
    private byte[] image;
    private Long score;
    private Integer commentCounter;

    private Integer offerCounter;

    public void extraHourPenalty(Long extraHours) {
        if (extraHours > 0)
            score -= extraHours;
        if (score < 0)
            status = WorkerStatus.SUSPENDED;
    }

    public void commentEffect(Long rating) {
        commentCounter++;
        score += rating;
    }
}