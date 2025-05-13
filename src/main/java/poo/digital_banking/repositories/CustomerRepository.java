package poo.digital_banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByNameContainsIgnoreCase(String keyword);
    boolean existsByEmail(String email);
}
