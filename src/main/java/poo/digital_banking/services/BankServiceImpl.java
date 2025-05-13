package poo.digital_banking.services;

import poo.digital_banking.dtos.*;
import poo.digital_banking.entities.*;

import poo.digital_banking.enums.AccountStatus;
import poo.digital_banking.enums.OperationType;
import poo.digital_banking.exceptions.BalanceNotSufficientException;
import poo.digital_banking.exceptions.BankAccountNotFoundException;
import poo.digital_banking.exceptions.CustomerNotFoundException;
import poo.digital_banking.mappers.BankMapperImpl;
import poo.digital_banking.repositories.BankAccountRepository;
import poo.digital_banking.repositories.CustomerRepository;
import poo.digital_banking.repositories.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    private final BankMapperImpl bankMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = bankMapper.fromCustomerDTO(customerDTO);
        customer.setId(UUID.randomUUID().toString());
        Customer savedCustomer = customerRepository.save(customer);
        return bankMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = bankMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankMapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(bankMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.findByNameContainsIgnoreCase(keyword);
        return customers.stream()
                .map(bankMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency("MAD");
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        CurrentAccount savedAccount = bankAccountRepository.save(currentAccount);
        return bankMapper.fromCurrentAccount(savedAccount);
    }

    @Override
    public SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCurrency("MAD");
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savedAccount = bankAccountRepository.save(savingAccount);
        return bankMapper.fromSavingAccount(savedAccount);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        Operation operation = new Operation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        Operation operation = new Operation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> getBankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(account -> {
            if (account instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) account;
                return bankMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) account;
                return bankMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> getCustomerAccounts(String customerId) throws CustomerNotFoundException {
        // Check if customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found");
        }

        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        return bankAccounts.stream().map(account -> {
            if (account instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) account;
                return bankMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) account;
                return bankMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        Page<Operation> operations = operationRepository.findByBankAccountIdOrderByDateDesc(accountId, PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(operations.getTotalPages());
        accountHistoryDTO.setOperationDTOS(
                operations.getContent().stream()
                        .map(bankMapper::fromOperation)
                        .collect(Collectors.toList())
        );

        return accountHistoryDTO;
    }
}
