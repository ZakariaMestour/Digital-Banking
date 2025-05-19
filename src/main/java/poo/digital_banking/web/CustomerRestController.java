package poo.digital_banking.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poo.digital_banking.dtos.BankAccountDTO;
import poo.digital_banking.dtos.CustomerDTO;
import poo.digital_banking.exceptions.CustomerNotFoundException;
import poo.digital_banking.services.BankService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private final BankService bankService;

    @GetMapping

    public List<CustomerDTO> customers() {
        return bankService.listCustomers();
    }

    @GetMapping("/search")
    public List<CustomerDTO> searchCustomers(@RequestParam String keyword) {
        return bankService.searchCustomers(keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String id) {
        try {
            return ResponseEntity.ok(bankService.getCustomer(id));
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String id, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return ResponseEntity.ok(bankService.updateCustomer(customerDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        try {
            bankService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<BankAccountDTO>> getCustomerAccounts(@PathVariable String id) {
        try {
            return ResponseEntity.ok(bankService.getCustomerAccounts(id));
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
