package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.security.Principal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();
    private final ConsoleService consoleService = new ConsoleService();
    private final UserService userService = new UserService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();

        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        if (currentUser.getToken() != null) {
            transferService.setAuthToken(currentUser.getToken());
            accountService.setAuthToken(currentUser.getToken());
            userService.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

        BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        System.out.println("Your current account balance is: " + balance);

	}

	private void viewTransferHistory() {
        Transfer[] transfers = transferService.getAllTransfers();
        for (Transfer transfer : transfers) {
            System.out.println(transfer);
        }
		
	}

	private void viewPendingRequests() {
        Transfer[] pendingTransfers = transferService.getPendingTransfers
                (accountService.getAccountByUserId(currentUser.getUser().getId()).getId());
        for (Transfer transfer : pendingTransfers) {
            System.out.println(transfer);
        }
		
	}

	private void sendBucks() {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------------------");

        // print all id and user names
        User[] allUsers = userService.getAllUsers();
        for(User user: allUsers){
            System.out.println(user.getId() + "      " + user.getUsername());
        }

        System.out.println("---------");

        long receiverId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");

        if(receiverId == 0){
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:");

        if(accountService.getAccountByUserId(currentUser.getUser().getId()).getBalance().compareTo(amount) < 0){
            System.out.println("Insufficient funds");
            return;
        }

        accountService.sendTeBucks(
                accountService.getAccountByUserId(currentUser.getUser().getId()).getId(),
                accountService.getAccountByUserId(receiverId).getId(),
                amount);
		
	}

	private void requestBucks() {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------------------");

        // print all id and user names
        User[] allUsers = userService.getAllUsers();
        for(User user: allUsers){
            System.out.println(user.getId() + "      " + user.getUsername());
        }

        System.out.println("---------");

        long senderId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):");

        if(senderId == 0){
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:");

        accountService.receiveTeBucks(
                accountService.getAccountByUserId(currentUser.getUser().getId()).getId(),
                accountService.getAccountByUserId(senderId).getId(),
                amount);
		
	}

}
