package poo.digital_banking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CC")
@AllArgsConstructor @NoArgsConstructor
@Data
public class CurrentAccount extends BankAccount{
    private Double overDraft;


}
