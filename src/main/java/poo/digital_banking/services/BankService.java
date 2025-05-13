package poo.digital_banking.services;

import poo.digital_banking.dtos.*;
import poo.digital_banking.exceptions.BalanceNotSufficientException;
import poo.digital_banking.exceptions.BankAccountNotFoundException;
import poo.digital_banking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(String customerId);
    CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<CustomerDTO> searchCustomers(String keyword);

    CurrentAccountDTO saveCurrentAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> getBankAccountList();
    List<BankAccountDTO> getCustomerAccounts(String customerId) throws CustomerNotFoundException;

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

}
