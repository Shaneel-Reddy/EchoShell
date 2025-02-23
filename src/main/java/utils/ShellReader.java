package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ShellReader {
    private static final List<String> BUILTIN_COMMANDS = Arrays.asList(
            "echo",
            "exit"
    );

    private final BufferedReader reader;
    private StringBuilder currentInput;

    public ShellReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.currentInput = new StringBuilder();
    }

    public String readLine() throws IOException {
        System.out.print("$ ");
        currentInput.setLength(0);

        while (true) {
            int c = reader.read();
            if (c == -1) {
                return null;
            }

            // Handle tab key (ASCII 9)
            if (c == 9) {
                handleTabCompletion();
                continue;
            }

            // Handle enter key
            if (c == '\n' || c == '\r') {
                System.out.println();
                return currentInput.toString();
            }

            // Handle regular characters
            currentInput.append((char) c);
            System.out.print((char) c);
        }
    }

    private void handleTabCompletion() {
        String input = currentInput.toString().trim();
        String completed = completeCommand(input);

        if (!completed.equals(input)) {
            // Clear current input
            while (currentInput.length() > 0) {
                System.out.print('\b');
                System.out.print(' ');
                System.out.print('\b');
                currentInput.setLength(currentInput.length() - 1);
            }

            // Print completed command
            System.out.print(completed);
            currentInput.append(completed);
        }
    }

    private String completeCommand(String partial) {
        if (partial.isEmpty()) {
            return "";
        }

        for (String cmd : BUILTIN_COMMANDS) {
            if (cmd.startsWith(partial)) {
                return cmd + " ";
            }
        }

        return partial;
    }
}