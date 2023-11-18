package br.grupo2.springdemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.grupo2.springdemo.domain.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByName(String name);

    Optional<Account> findOneByEmail(String email);

        @Query(value = "SELECT * FROM account a " +
                "JOIN account_role ac_roles ON a.id = ac_roles.account_id " +
                "WHERE ac_roles.roles = ?1",
                countQuery = "SELECT COUNT(*) FROM account_role WHERE roles = ?1",
                nativeQuery = true)
        Page<Account> findAllByRole(String role, Pageable pageable);


        @Query(value = "SELECT * FROM account a " +
                "LEFT JOIN (" +
                "    SELECT T.id, SUM(T.num) as total FROM (" +
                "        SELECT a.id, COUNT(*) as num FROM account a " +
                "        JOIN question_dislike dislikes ON a.id = dislikes.account_id GROUP BY a.id " +
                "        UNION ALL " +
                "        SELECT a.id, COUNT(*) as num FROM account a " +
                "        JOIN question_like likes ON a.id = likes.account_id GROUP BY a.id " +
                "        UNION ALL " +
                "        SELECT a.id, COUNT(*) as num FROM account a " +
                "        JOIN answer_like likes ON a.id = likes.account_id GROUP BY a.id " +
                "        UNION ALL " +
                "        SELECT a.id, COUNT(*) as num FROM account a " +
                "        JOIN answer_dislike dislikes ON a.id = dislikes.account_id GROUP BY a.id" +
                "    ) T GROUP BY T.id" +
                ") U ON a.id = U.id ORDER BY U.total DESC NULLS LAST",
                countQuery = "SELECT COUNT(*) FROM account",
                nativeQuery = true)
        Page<Account> findAllSortByMostVotes(Pageable pageable);


        @Query(value = "SELECT * FROM account a " +
                "LEFT JOIN (" +
                "    SELECT U.id, SUM(U.num) as total FROM (" +
                "        SELECT u.id, COUNT(*) as num FROM account u " +
                "        JOIN answer a ON u.id = a.last_modified_by " +
                "        WHERE a.created_date <> a.last_modified_date " +
                "        GROUP BY u.id " +
                "        UNION ALL " +
                "        SELECT u.id, COUNT(*) as num FROM account u " +
                "        JOIN question q ON u.id = q.last_modified_by " +
                "        WHERE q.created_date <> q.last_modified_date " +
                "        GROUP BY u.id" +
                "    ) U GROUP BY U.id" +
                ") T ON a.id = T.id ORDER BY T.total DESC NULLS LAST",
                countQuery = "SELECT COUNT(*) FROM account",
                nativeQuery = true)
        Page<Account> findAllSortByMostEdits(Pageable pageable);
}
