package com.configurations.security.postgresql;

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
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Profile(Profiles.POSTGRESQL)
@Configuration
@EnableWebSecurity
public class SecurityPostgreSQL extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    public SecurityPostgreSQL(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/profile").authenticated()
                .antMatchers("/*").denyAll()
                .antMatchers("/**").denyAll()
                .and()
                .formLogin()
                .defaultSuccessUrl("/profile");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(postgresqlUserDetailsManager()).passwordEncoder(passwordEncoder);
    }

    @Bean
    public JdbcUserDetailsManager postgresqlUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        UserDetails userDetails = User
                .withUsername("test")
                .password("$2a$10$8fd8Flsoq5fs/VxFWd8Z6ux0F1xHwCuWsTH/W8RMdE4VSi9IMCkci")
                .authorities("ROLE_USER")
                .build();
        jdbcUserDetailsManager.createUser(userDetails);
        return jdbcUserDetailsManager;
    }

    @PreDestroy
    public void teardown() {
        postgresqlUserDetailsManager().deleteUser("test");
    }
}
