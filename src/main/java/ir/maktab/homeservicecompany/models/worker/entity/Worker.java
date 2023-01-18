package ir.maktab.homeservicecompany.models.worker.entity;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.utils.security.config.PasswordConfig;
import ir.maktab.homeservicecompany.utils.security.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    public Worker(String firstName, String lastName, String password, String email, byte[] image) {
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.credit = 0D;
        this.status = WorkerStatus.AWAITING_FOR_CONFIRM;
        this.image = image;
        this.score = 0L;
        this.commentCounter = 0;
        this.offerCounter = 0;
        this.completedTaskCounter =0;
        this.role=Role.ROLE_NEW_WORKER;
    }

    private String firstName;
    private String lastName;
    private String password;
    @Column(unique = true)
    @Email
    private String email;
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

    public void setFirstName(String firstName) {
        if (Strings.isNullOrEmpty(firstName))
            throw new IllegalArgumentException("first name cannot be null or empty.");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (Strings.isNullOrEmpty(lastName))
            throw new IllegalArgumentException("last name cannot be null or empty.");
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$"))
            throw new IllegalArgumentException(
                    "password must have exactly 8 characters and contains at least 1 digit and 1 alphabet.");
        this.password = PasswordConfig.passwordEncoder().encode(password);
    }

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