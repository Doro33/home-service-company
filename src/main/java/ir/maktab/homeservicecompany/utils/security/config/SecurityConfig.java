package ir.maktab.homeservicecompany.utils.security.config;

import ir.maktab.homeservicecompany.utils.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserService userSer;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, UserService userSer) {
        this.passwordEncoder = passwordEncoder;
        this.userSer = userSer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/client/activateAccount/**").hasRole("NEW_CLIENT")
                .requestMatchers("/worker/activateAccount/**").hasRole("NEW_WORKER")
                .requestMatchers("/worker/**").hasRole("WORKER")
                .requestMatchers("/client/**").hasRole("CLIENT")
                .requestMatchers("/general/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Autowired
    public void globalConfiguration(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userSer).
                    passwordEncoder(passwordEncoder);
    }
}