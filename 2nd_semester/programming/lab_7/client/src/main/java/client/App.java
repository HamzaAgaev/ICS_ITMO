package client;

import client.exceptions.ClientManagerException;
import client.exceptions.SendingException;
import client.inputtools.ScriptScanner;
import client.netclient.ClientAuthorizationManager;
import client.netclient.ClientManager;
import common.data.LoginStatus;
import common.data.RegisterStatus;

import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        boolean canContinueClient = true;
        ClientManager clientManager = ClientManager.getInstance();
        try {
            clientManager.start();
        } catch (ClientManagerException clientManagerException) {
            System.out.println(clientManagerException);
        }
        if (canContinueClient) {
            ClientAuthorizationManager clientAuthorizationManager = ClientAuthorizationManager.getInstance();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String selectedCommand;
                System.out.print("Enter command (login | register | exit): ");
                selectedCommand = scanner.nextLine().toLowerCase();
                try {
                    if (selectedCommand.equals("login") || selectedCommand.equals("register")) {
                        String username = "";
                        String password = "";
                        while (username.length() < 3) {
                            System.out.print("Enter username: ");
                            username = scanner.nextLine();
                            if (username.length() < 3) {
                                System.out.println("Username is too short!");
                            }
                        }
                        while (password.length() < 4) {
                            System.out.print("Enter password: ");
                            password = scanner.nextLine();
                            if (password.length() < 4) {
                                System.out.println("Password is too short!");
                            }
                        }
                        if (selectedCommand.equals("login")) {
                            LoginStatus loginStatus = clientAuthorizationManager.login(username, password);
                            if (loginStatus == LoginStatus.SUCCESS) {
                                System.out.println("Success Login!");
                                break;
                            } else {
                                System.out.println("Failed to login. Login Status: " + loginStatus + ".");
                            }
                        } else {
                            RegisterStatus registerStatus = clientAuthorizationManager.register(username, password);
                            if (registerStatus == RegisterStatus.SUCCESS) {
                                System.out.println("Success Registration! Now login in your account.");
                            } else {
                                System.out.println("Failed to register. Register Status: " + registerStatus + ".");
                            }
                        }
                    } else if (selectedCommand.equals("exit")) {
                        canContinueClient = false;
                        break;
                    } else {
                        System.out.println("Invalid command. Please, try again.");
                    }
                } catch (SendingException sendingException) {
                    System.out.println(sendingException);
                }
            }
        }
        if (canContinueClient) {
            ScriptScanner scriptScanner = new ScriptScanner();
            scriptScanner.run();
        }
    }
}
