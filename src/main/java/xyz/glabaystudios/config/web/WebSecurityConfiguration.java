package xyz.glabaystudios.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import xyz.glabaystudios.wbc.tracker.services.web.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(requests -> {
                    try {
                        requests
                                .requestMatchers("/acp/**").hasRole("DEVELOPER")
                                .requestMatchers("/admin/**").hasAnyRole("DEVELOPER", "ADMIN")
                                .requestMatchers("/callbacks/**").hasRole("WBC")
                                .requestMatchers("/lilblu/**").hasRole("DEVELOPER")
                                .anyRequest().authenticated()
                                .and()
                                .formLogin().permitAll()
                                .and()
                                .logout().permitAll()
                        ;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
        ;


        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserDetailsService());
        authProvider.setPasswordEncoder(getBCryptPasswordEncoder());

        return authProvider;
    }
}
