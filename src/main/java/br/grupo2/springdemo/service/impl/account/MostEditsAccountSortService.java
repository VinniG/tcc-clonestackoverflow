package br.grupo2.springdemo.service.impl.account;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.repository.AccountRepository;
import br.grupo2.springdemo.service.AccountSortService;

@Service
@AllArgsConstructor
public class MostEditsAccountSortService implements AccountSortService {

    private final AccountRepository accountRepository;

    public Page<Account> sort(Pageable pageable) {
        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return accountRepository.findAllSortByMostEdits(unsortedPageable);
    }

    public boolean isSuitableFor(AccountSortType sortType) {
        return AccountSortType.MOST_EDITS.equals(sortType);
    }

}
