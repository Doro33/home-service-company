package ir.maktab.homeservicecompany.utils.config;

import ir.maktab.homeservicecompany.utils.config.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserService userSer;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder passwordEncoder, UserService userSer) {
        this.passwordEncoder = passwordEncoder;
        this.userSer = userSer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/worker/**").hasRole("WORKER")
                .requestMatchers("/client/**").hasRole("CLIENT")
                .requestMatchers("/general").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth){
        try {
            auth.userDetailsService(email ->
                    userSer.loadUserByUsername(email)
            ).passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}