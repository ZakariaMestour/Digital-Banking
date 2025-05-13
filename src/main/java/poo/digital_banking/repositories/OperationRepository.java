package poo.digital_banking.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.Operation;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
    List<Operation> findByBankAccountId(String accountId);
    Page<Operation> findByBankAccountIdOrderByDateDesc(String accountId, Pageable pageable);
}
