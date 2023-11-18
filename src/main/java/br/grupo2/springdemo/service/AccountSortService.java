package br.grupo2.springdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.service.impl.account.AccountSortType;

public interface AccountSortService {

    Page<Account> sort(Pageable pageable);

    boolean isSuitableFor(AccountSortType sortType);

}
