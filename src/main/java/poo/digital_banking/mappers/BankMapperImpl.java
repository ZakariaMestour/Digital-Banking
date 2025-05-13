package poo.digital_banking.mappers;

import org.springframework.stereotype.Service;
import poo.digital_banking.dtos.*;
import poo.digital_banking.entities.*;
@Service
public class BankMapperImpl implements BankMapper {
    @Override
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

    @Override
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        return customer;
    }

    @Override
    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        savingAccountDTO.setId(savingAccount.getId());
        savingAccountDTO.setCreatedAt(savingAccount.getCreatedAt());
        savingAccountDTO.setBalance(savingAccount.getBalance());
        savingAccountDTO.setStatus(savingAccount.getStatus());
        savingAccountDTO.setCurrency(savingAccount.getCurrency());
        savingAccountDTO.setInterestRate(savingAccount.getInterestRate());
        savingAccountDTO.setType("SAVING");
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        return savingAccountDTO;
    }

    @Override
    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        currentAccountDTO.setId(currentAccount.getId());
        currentAccountDTO.setCreatedAt(currentAccount.getCreatedAt());
        currentAccountDTO.setBalance(currentAccount.getBalance());
        currentAccountDTO.setStatus(currentAccount.getStatus());
        currentAccountDTO.setCurrency(currentAccount.getCurrency());
        currentAccountDTO.setOverDraft(currentAccount.getOverDraft());
        currentAccountDTO.setType("CURRENT");
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        return currentAccountDTO;
    }

    @Override
    public OperationDTO fromOperation(Operation operation) {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setId(operation.getId());
        operationDTO.setDate(operation.getDate());
        operationDTO.setAmount(operation.getAmount());
        operationDTO.setType(operation.getType());
        operationDTO.setAccountId(operation.getBankAccount().getId());
        return operationDTO;
    }
}
