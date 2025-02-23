import java.io.Console;
import java.io.File;
import java.util.List;
import commands.CommandExecutor;
import commands.EchoCommand;
import commands.CdCommand;
import commands.TypeCommand;
import utils.Tokenizer;

public class Main {
    private static File pwd = new File(System.getProperty("user.dir"));
    private static final String[] BUILTIN_COMMANDS = {"echo", "exit"};

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available");
            System.exit(1);
        }

        while (true) {
            System.out.print("$ ");
            StringBuilder input = new StringBuilder();

            while (true) {
                int ch = System.in.read();
                if (ch == '\n') { // Enter key
                    break;
                } else if (ch == '\t') { // Tab key for autocomplete
                    String completed = autocomplete(input.toString());
                    if (!completed.equals(input.toString())) {
                        input.setLength(0);
                        input.append(completed).append(" ");
                        System.out.print("\r$ " + input); // Reprint with updated input
                    }
                } else if (ch == '\b' && input.length() > 0) { // Handle backspace
                    input.deleteCharAt(input.length() - 1);
                    System.out.print("\r$ " + input + " "); // Clear last character
                } else {
                    input.append((char) ch);
                    System.out.print((char) ch);
                }
            }

            String commandLine = input.toString().trim();
            if (commandLine.isEmpty()) continue;

            List<String> tokens = Tokenizer.tokenizeInput(commandLine);
            String command = tokens.getFirst();
            String[] commandArgs = tokens.subList(1, tokens.size()).toArray(new String[0]);

            if ("exit".equals(command) && commandArgs.length == 1 && "0".equals(commandArgs[0])) {
                System.exit(0);
            }

            switch (command) {
                case "echo" -> {
                    System.out.println(String.join(" ", commandArgs));
                }
                case "pwd" -> System.out.println(pwd.getAbsolutePath());
                case "cd" -> pwd = CdCommand.handle(commandArgs, pwd);
                case "type" -> TypeCommand.handle(commandArgs);
                default -> System.out.println(command + ": command not found");
            }
        }
    }

    private static String autocomplete(String input) {
        if (input.isEmpty()) {
            return input; // Do nothing if no input
        }
        for (String cmd : BUILTIN_COMMANDS) {
            if (cmd.startsWith(input)) {
                return cmd;
            }
        }
        return input;
    }
}
