package poo.digital_banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.BankAccount;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByCustomerId(String customerId);
}
