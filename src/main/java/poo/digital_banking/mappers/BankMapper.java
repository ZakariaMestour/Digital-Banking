package poo.digital_banking.mappers;
import poo.digital_banking.dtos.*;
import poo.digital_banking.entities.*;
public interface BankMapper {
    CustomerDTO fromCustomer(Customer customer);
    Customer fromCustomerDTO(CustomerDTO customerDTO);

    SavingAccountDTO fromSavingAccount(SavingAccount savingAccount);
    CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount);

    OperationDTO fromOperation(Operation operation);
}
