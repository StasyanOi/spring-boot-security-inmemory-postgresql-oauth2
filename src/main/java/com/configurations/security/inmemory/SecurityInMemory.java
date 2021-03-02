package com.configurations.security.inmemory;

import com.constants.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Profile(Profiles.INMEMORY)
@Configuration
@EnableWebSecurity
public class SecurityInMemory extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public SecurityInMemory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/profile").authenticated()
                .antMatchers("/*").denyAll()
                .antMatchers("/**").denyAll()
                .and()
//              .httpBasic()
                .formLogin()
                .defaultSuccessUrl("/profile");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager()).passwordEncoder(passwordEncoder);
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        UserDetails userDetails = User
                .withUsername("test")
                .password("$2a$10$8fd8Flsoq5fs/VxFWd8Z6ux0F1xHwCuWsTH/W8RMdE4VSi9IMCkci")
                .authorities("ROLE_USER")
                .build();
        inMemoryUserDetailsManager.createUser(userDetails);
        return inMemoryUserDetailsManager;
    }
}
