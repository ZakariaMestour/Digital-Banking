package poo.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poo.digital_banking.enums.AccountStatus;

import java.util.Date;
@Data @AllArgsConstructor @NoArgsConstructor
public class BankAccountDTO {
    private String id;
    private Date createdAt;
    private double balance;
    private AccountStatus status;
    private String currency;
    private String type;
    private CustomerDTO customerDTO;
}
