package client.inputtools;

import client.exceptions.ExecutionProhibition;
import client.commands.CommandManager;
import client.commands.ExecutionMessage;
import client.exceptions.ExitException;

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
        String selectedCommand;
        String[] scannedStringArray = scanner.nextLine().split(" ", 2);
        if (scannedStringArray.length == 2) {
            selectedCommand = scannedStringArray[0].toLowerCase() + " " + scannedStringArray[1];
        } else {
            selectedCommand = scannedStringArray[0].toLowerCase();
        }
        return selectedCommand;
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
    }
}
