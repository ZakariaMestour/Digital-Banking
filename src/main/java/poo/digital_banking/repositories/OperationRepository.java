package poo.digital_banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poo.digital_banking.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
