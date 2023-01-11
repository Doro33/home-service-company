package ir.maktab.homeservicecompany.models.client.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@NotNull
public class Client extends BaseEntity {
        public Client(String firstName, String lastName, String email, String password) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.password = password;
                this.createdAt = LocalDateTime.now();
                this.credit = 0D;
                this.orderCounter=0;
        }

        private String firstName;
        private String lastName;
        @Column(unique = true)
        @Email
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$" ,
                message = """
                        password must contain at least 1 uppercase or lowercase and 1 digit.
                        password must have exactly 8 characters.
                        """)
        private String password;
        private LocalDateTime createdAt;
        private Double credit;
        private Integer orderCounter;
    }