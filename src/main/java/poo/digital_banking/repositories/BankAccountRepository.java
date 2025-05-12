package poo.digital_banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
}
