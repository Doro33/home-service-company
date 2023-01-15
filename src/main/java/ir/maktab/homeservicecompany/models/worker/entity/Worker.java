package ir.maktab.homeservicecompany.models.worker.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.utils.config.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@NotNull
public class Worker extends BaseEntity implements UserDetails {
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
        this.completedTaskCounter =0;
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

    private Integer completedTaskCounter;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    public void completedRequestEffect(){
        completedTaskCounter++;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}