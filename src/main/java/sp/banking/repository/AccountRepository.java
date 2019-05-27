package sp.banking.repository;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sp.banking.domain.Account;

import java.util.List;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("FROM Account WHERE accountId IN (:from_account_id, :to_account_id)")
    List<Account> getAccountsByIdsAndLockRow(@Param("from_account_id") Long fromAccountId,
                                             @Param("to_account_id") Long toAccountId);
}
