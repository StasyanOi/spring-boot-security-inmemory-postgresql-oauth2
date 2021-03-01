package com.configurations.security.oauth2;

import com.constants.Profiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile(Profiles.OAUTH2)
@Configuration
@EnableWebSecurity
public class SecurityOAuth2 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/profile").authenticated()
                .antMatchers("/*").denyAll()
                .antMatchers("/**").denyAll()
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/profile");
    }
}
