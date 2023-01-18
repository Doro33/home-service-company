package ir.maktab.homeservicecompany.models.client.entity;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.utils.security.config.PasswordConfig;
import ir.maktab.homeservicecompany.utils.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@Setter
@ToString
@NotNull
public class Client extends BaseEntity implements UserDetails {

    public Client(String firstName, String lastName, String password, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.credit = 0D;
        this.requestCounter = 0;
        this.setRole(Role.ROLE_NEW_CLIENT);
    }

    private String firstName;
    private String lastName;
    @Column(unique = true)
    @Email
    private String email;

    private String password;
    private LocalDateTime createdAt;
    private Double credit;
    private Integer requestCounter;

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