package utils;

import java.util.Scanner;

public class NamedScanner {
    private final String filename;
    private final Scanner scanner;
    private final Boolean isInteractiveMode;

    public NamedScanner(String filename, Scanner scanner, Boolean isInteractiveMode) {
        this.filename = filename;
        this.scanner = scanner;
        this.isInteractiveMode = isInteractiveMode;
    }

    public String getFilename() {
        return filename;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Boolean getIsInteractiveMode() {
        return isInteractiveMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedScanner namedScanner = (NamedScanner) o;
        return this.getFilename().equals(namedScanner.getFilename()) && this.getIsInteractiveMode().equals(namedScanner.getIsInteractiveMode());
    }
}
