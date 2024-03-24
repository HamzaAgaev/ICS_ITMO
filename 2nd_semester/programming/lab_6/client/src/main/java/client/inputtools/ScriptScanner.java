package client.inputtools;

import client.exceptions.ClientManagerException;
import client.exceptions.ExecutionProhibition;
import client.commands.CommandManager;
import client.commands.ExecutionMessage;
import client.exceptions.ExitException;
import client.netclient.ClientManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Scanner;

public class ScriptScanner implements Iterator<String> {
    private final Scanner scanner;
    private static Boolean needToExit = false;
    private Boolean isInteractiveMode;
    private static ArrayDeque<NamedScanner> executingScriptsIS = new ArrayDeque<>();

    public ScriptScanner() {
        scanner = new Scanner(System.in);
        isInteractiveMode = true;
        executingScriptsIS.add(new NamedScanner("", scanner, isInteractiveMode));
    }

    public ScriptScanner(String pathToFile) throws FileNotFoundException, NullPointerException, ExecutionProhibition {
        FileInputStream scannerFileIS = new FileInputStream(pathToFile);
        scanner = new Scanner(scannerFileIS);
        isInteractiveMode = false;
        if (executingScriptsIS.contains(new NamedScanner(pathToFile, scanner, isInteractiveMode))) {
            throw new ExecutionProhibition("File " + pathToFile + " is already executing.");
        }
        executingScriptsIS.add(new NamedScanner(pathToFile, scanner, isInteractiveMode));
    }

    @Override
    public boolean hasNext() {
        if (!needToExit) {
            return scanner.hasNextLine();
        } else {
            return false;
        }
    }

    @Override
    public String next() {
        return scanner.nextLine();
    }

    public static ArrayDeque<NamedScanner> getExecutingScriptsIS() {
        return executingScriptsIS;
    }

    public Boolean getIsInteractiveMode() {
        return isInteractiveMode;
    }

    public void closeScanner() {
        scanner.close();
    }

    public void run() {
        CommandManager commandManager = CommandManager.getInstance();
        ClientManager clientManager = ClientManager.getInstance();
        try {
            clientManager.start();
            if (getIsInteractiveMode()) {
                System.out.print("Enter command (\"help\" to see command list): ");
            }
            while (hasNext()) {
                try {
                    ExecutionMessage executionMessage = commandManager.runCommand(next());
                    System.out.println(executionMessage.getMessage());
                } catch (ExitException exitException) {
                    needToExit = true;
                    break;
                } catch (Exception exception) {
                    System.out.println(exception);
                }
                if (getIsInteractiveMode() && !needToExit) {
                    System.out.print("Enter command (\"help\" to see command list): ");
                }
            }
            closeScanner();
            executingScriptsIS.removeLast();
            clientManager.close();
        } catch (ClientManagerException clientManagerException) {
            System.out.println(clientManagerException);
        }
    }
}
