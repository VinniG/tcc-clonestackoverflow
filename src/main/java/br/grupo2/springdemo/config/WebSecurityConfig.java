package br.grupo2.springdemo.config;

import static br.grupo2.springdemo.controller.ControllerConstants.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import br.grupo2.springdemo.audit.AuditorAwareImpl;
import br.grupo2.springdemo.domain.Account;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<Account> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(LOGIN_PATH, REGISTRATION_PATH, "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();

                // Configuração adicional para permitir o acesso ao console H2
                // http.csrf().disable();
                http.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(
                "/resources/**",
                "/public/**");
    }

}

