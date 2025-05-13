package poo.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poo.digital_banking.enums.OperationType;

import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class OperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private String accountId;
}
