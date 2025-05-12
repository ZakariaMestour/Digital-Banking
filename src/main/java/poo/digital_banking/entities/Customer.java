package poo.digital_banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Builder
@Data
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER)
    private List<BankAccount> bankAccounts=new ArrayList<>();
}
