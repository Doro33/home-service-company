package ir.maktab.homeservicecompany.models.admin.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.utils.security.config.PasswordConfig;
import ir.maktab.homeservicecompany.utils.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Admin extends BaseEntity implements UserDetails {

    public Admin(String email, String password) {
        this.email = email;
        setPassword(password);
        this.credit = 0D;
        this.role = Role.ROLE_ADMIN;
    }

    @Email
    private String email;
    private String password;
    private Double credit;
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private Role role;

    public void setPassword(String password) {
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