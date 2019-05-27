package sp.banking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sp.banking.domain.Transaction;

import java.util.List;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    @Query("FROM Transaction WHERE fromAccount.accountId = :account")
    List<Transaction> findAllByFromAccount(@Param("account") Long account, Pageable pageable);
}
