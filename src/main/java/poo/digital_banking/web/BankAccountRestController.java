package poo.digital_banking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poo.digital_banking.dtos.AccountHistoryDTO;
import poo.digital_banking.dtos.BankAccountDTO;
import poo.digital_banking.dtos.CurrentAccountDTO;
import poo.digital_banking.dtos.SavingAccountDTO;
import poo.digital_banking.exceptions.BalanceNotSufficientException;
import poo.digital_banking.exceptions.BankAccountNotFoundException;
import poo.digital_banking.exceptions.CustomerNotFoundException;
import poo.digital_banking.services.BankService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private final BankService bankService;

    @GetMapping
    public List<BankAccountDTO> bankAccounts() {
        return bankService.getBankAccountList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable String id) {
        try {
            return ResponseEntity.ok(bankService.getBankAccount(id));
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/operations")
    public ResponseEntity<AccountHistoryDTO> getAccountHistory(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(bankService.getAccountHistory(id, page, size));
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestBody Map<String, Object> requestMap) {
        try {
            String accountId = (String) requestMap.get("accountId");
            double amount = Double.parseDouble(requestMap.get("amount").toString());
            String description = (String) requestMap.get("description");

            bankService.debit(accountId, amount, description);
            return ResponseEntity.ok().body("Debit operation successful");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestBody Map<String, Object> requestMap) {
        try {
            String accountId = (String) requestMap.get("accountId");
            double amount = Double.parseDouble(requestMap.get("amount").toString());
            String description = (String) requestMap.get("description");

            bankService.credit(accountId, amount, description);
            return ResponseEntity.ok().body("Credit operation successful");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> requestMap) {
        try {
            String sourceAccountId = (String) requestMap.get("sourceAccountId");
            String destinationAccountId = (String) requestMap.get("destinationAccountId");
            double amount = Double.parseDouble(requestMap.get("amount").toString());

            bankService.transfer(sourceAccountId, destinationAccountId, amount);
            return ResponseEntity.ok().body("Transfer operation successful");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/current")
    public ResponseEntity<CurrentAccountDTO> createCurrentAccount(@RequestBody Map<String, Object> requestMap) {
        try {
            String customerId = (String) requestMap.get("customerId");
            double initialBalance = Double.parseDouble(requestMap.get("initialBalance").toString());
            double overDraft = Double.parseDouble(requestMap.get("overDraft").toString());

            CurrentAccountDTO accountDTO = bankService.saveCurrentAccount(initialBalance, overDraft, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/saving")
    public ResponseEntity<SavingAccountDTO> createSavingAccount(@RequestBody Map<String, Object> requestMap) {
        try {
            String customerId = (String) requestMap.get("customerId");
            double initialBalance = Double.parseDouble(requestMap.get("initialBalance").toString());
            double interestRate = Double.parseDouble(requestMap.get("interestRate").toString());

            SavingAccountDTO accountDTO = bankService.saveSavingAccount(initialBalance, interestRate, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
