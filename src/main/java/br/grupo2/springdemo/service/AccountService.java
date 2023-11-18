package br.grupo2.springdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.grupo2.springdemo.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    // Encontra uma conta pelo ID
    Optional<Account> findById(Long id);

    // Encontra uma conta pelo endereço de e-mail
    Optional<Account> findByEmail(String email);

    // Encontra contas pelo nome
    List<Account> findByName(String name);

    // Retorna todas as contas
    List<Account> findAll();

    // Retorna todas as contas com suporte à paginação
    Page<Account> findAll(Pageable pageable);

    // Salva uma nova conta ou atualiza uma existente
    Account save(Account account);

    // Exclui uma conta pelo ID
    void deleteById(Long id);
}
