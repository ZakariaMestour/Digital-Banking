package poo.digital_banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
