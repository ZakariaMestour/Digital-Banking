package poo.digital_banking.dtos;

import lombok.Data;

@Data
public class AccountOperationDTO {
    private String accountId;
    private double amount;
    private String description;
}
