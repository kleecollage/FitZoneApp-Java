package gm.fit_zone;

import gm.fit_zone.Service.ClientService;
import gm.fit_zone.Service.IClientService;
import gm.fit_zone.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class FitZoneApplication implements CommandLineRunner {
	private final IClientService clientService;
	private static final Logger logger = LoggerFactory.getLogger(FitZoneApplication.class);
	String newLine = System.lineSeparator();

	@Autowired
    public FitZoneApplication(ClientService clientService) {
        this.clientService = clientService;
    }

    public static void main(String[] args) {
		logger.info("Initializing Application");
		SpringApplication.run(FitZoneApplication.class, args);
		logger.info("Application Ended");
	}

	@Override
	public void run(String... args) throws Exception {
		fitZoneApp();
	}

	private void fitZoneApp() {
		var exit = false;
		var console = new Scanner(System.in);
		while (!exit) {
			try {
				var option = showMenu(console);
				exit = executeOptions(console, option);
			} catch (Exception e) {
				System.out.println("Error executing options: " + e.getMessage());
			}
		}
	}

	// RETURN OPTION SELECTED
	private int showMenu(Scanner console) {
		logger.info("\n *** Welcome to FitZone! ***\n");
		logger.info("""
                Menu:
                \t1. Clients List
                \t2. Search Client By ID
                \t3. Add New Client
                \t4. Update Client
                \t5. Delete Client
                \t6. Exit
                Enter your choice:\s""");
		return Integer.parseInt(console.nextLine());
	}

	// OPTIONS LOGIC
	private boolean executeOptions(Scanner console, int option) {
		var exit = false;

		switch (option) {
			case 1 -> {
				System.out.println("\n.::List Clients::.\n");
				clientService.listClients().forEach(client -> {
					logger.info(client.toString() + newLine);
				});
			}
			case 2 -> {
				System.out.println("\n.::Search Client By ID::.\n");
				isInDb(clientService, console);
			}
			case 3 -> {
				logger.info("\n.::Add New Client::.\n");
				var newClient = clientForm(console);
				clientService.saveClient(newClient);
				logger.info("New Client: " + newClient + newLine);
			}
			case 4 -> {
				logger.info("\n.::Update Client::.\n");
				var idClient = isInDb(clientService, console);
				if (idClient != 0) {
					var clientToUpdate = clientForm(console);
					clientToUpdate.setId(idClient);
					clientService.saveClient(clientToUpdate);
					logger.info("Client updated: " + clientToUpdate + newLine);
				} else {
					logger.info("Try with other ID");
				}
			}
			case 5 -> {
				logger.info("\n.::Delete Client::.\n");
				var idClient = isInDb(clientService, console);
				if (idClient != 0) {
					var clientToDelete = clientService.getClientById(idClient);
					clientService.deleteClientById(clientToDelete);
					logger.info("Client deleted");
				} else {
					logger.info("Try with other ID" + newLine);
				}
			}
			case 6 -> exit = true;
			default -> logger.info("Invalid option");
		}

		return exit;
	}

	private int isInDb(IClientService clientService, Scanner console) {
		System.out.print("Enter client id: ");
		var idClient = Integer.parseInt(console.nextLine());
		var clientFound = clientService.getClientById(idClient);
		if (clientFound != null)
			System.out.println("Current Client: " + clientFound);
		else {
			System.out.println("OOPS. Client not found");
			return 0;
		}

		return idClient;
	}

	private Client clientForm(Scanner console) {
		logger.info("Enter client name: ");
		var nameClient = console.nextLine();

		logger.info("Enter client lastname: ");
		var lastnameClient = console.nextLine();

		logger.info("Enter the client membership number: ");
		var membershipClient = Integer.parseInt(console.nextLine());

		var client = new Client();
		client.setName(nameClient);
		client.setLastname(lastnameClient);
		client.setMembership(membershipClient);

		return client;
	}
}
