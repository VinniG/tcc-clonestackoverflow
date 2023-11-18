package br.grupo2.springdemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.grupo2.springdemo.controller.exception.AccountNotFoundException;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.security.UserPrincipal;
import br.grupo2.springdemo.service.AccountService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca a conta pelo e-mail
        Account account = accountService.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(email));

        // Cria um UserDetails com os detalhes da conta
        UserDetails userDetails = User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRoles().stream().map(Enum::toString).toArray(String[]::new))
                .build();

        // Retorna um UserPrincipal que estende UserDetails e armazena o ID da conta
        return new UserPrincipal(userDetails, account.getId());
    }

}
