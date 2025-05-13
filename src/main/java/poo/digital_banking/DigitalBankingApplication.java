package poo.digital_banking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import poo.digital_banking.dtos.BankAccountDTO;
import poo.digital_banking.dtos.CustomerDTO;
import poo.digital_banking.exceptions.CustomerNotFoundException;
import poo.digital_banking.services.BankService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(DigitalBankingApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(BankService bankService) {
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setId(UUID.randomUUID().toString());
				customer.setName(name);
				customer.setEmail(name.toLowerCase() + "@gmail.com");
				bankService.saveCustomer(customer);
			});

			List<CustomerDTO> customers = bankService.listCustomers();

			customers.forEach(customer -> {
				try {
					// Create a current account for each customer
					bankService.saveCurrentAccount(Math.random() * 90000, 9000, customer.getId());

					// Create a saving account for each customer
					bankService.saveSavingAccount(Math.random() * 120000, 5.5, customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});

			// Create some operations
			List<BankAccountDTO> bankAccounts = bankService.getBankAccountList();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 10; i++) {
					try {
						String accountId = bankAccount.getId();
						bankService.credit(accountId, 10000 + Math.random() * 120000, "Credit operation");
						bankService.debit(accountId, 1000 + Math.random() * 9000, "Debit operation");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

}
