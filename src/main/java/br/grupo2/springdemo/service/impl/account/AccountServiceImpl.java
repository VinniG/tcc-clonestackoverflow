package br.grupo2.springdemo.service.impl.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.domain.Image;
import br.grupo2.springdemo.domain.Role;
import br.grupo2.springdemo.repository.AccountRepository;
import br.grupo2.springdemo.service.AccountService;
import br.grupo2.springdemo.service.ImageService;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ImageService imageService;

    // Retorna uma conta pelo ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    // Retorna uma conta pelo e-mail
    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    // Retorna uma lista de contas pelo nome
    @Override
    @Transactional(readOnly = true)
    public List<Account> findByName(String name) {
        return accountRepository.findByName(name);
    }

    // Retorna todas as contas
    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    // Retorna todas as contas paginadas
    @Override
    @Transactional(readOnly = true)
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    // Obtém um avatar do Gravatar
    public Image getAvatar(int avatarSize, String userEmail) {
        Image avatar = new Image();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(userEmail.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
            String avatarSource = "https://www.gravatar.com/avatar/%s?d=identicon&s=%d";
            String avatarUrl = String.format(avatarSource, hash, avatarSize);

            try(InputStream is = new URL(avatarUrl).openStream()) {
                String avatarData = Base64.getEncoder().encodeToString(is.readAllBytes());
                avatar.setData(avatarData);
            } catch (MalformedURLException ex) {
                throw new RuntimeException("There is a problem while downloading image", ex);
            } catch (IOException ioEx) {
                throw new RuntimeException("Problem with saving image", ioEx);
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        return avatar;
    }

    // Salva uma conta
    @Override
    @Transactional
    public Account save(Account account) {
        if (account.getId() != null) {
            // Atualiza uma conta existente
            Account accountToPut = accountRepository.findById(account.getId()).orElseThrow();
            accountToPut.setEmail(account.getEmail());
            accountToPut.setName(account.getName());
            accountToPut.setRoles(account.getRoles());
            return accountRepository.save(accountToPut);
        }
        // Cria uma nova conta
        int avatarSize = 164;
        Image avatar = getAvatar(avatarSize, account.getEmail());
        account.setAvatar(avatar);
        account.addRole(Role.USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    // Exclui uma conta pelo ID
    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            // Log para informar que a entidade não existe
            log.info("Delete non-existing entity with id=" + id, ex);
        }
    }
}
