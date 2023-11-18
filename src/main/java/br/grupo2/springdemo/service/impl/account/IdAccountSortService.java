package br.grupo2.springdemo.service.impl.account;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.repository.AccountRepository;
import br.grupo2.springdemo.service.AccountSortService;

@Service
@AllArgsConstructor
public class IdAccountSortService implements AccountSortService {

    private final AccountRepository accountRepository;

    @Override
    public Page<Account> sort(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public boolean isSuitableFor(AccountSortType sortType) {
        return AccountSortType.ID.equals(sortType);
    }
}
