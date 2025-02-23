import java.io.File;
import java.io.IOException;
import java.util.List;
import commands.CommandExecutor;
import commands.EchoCommand;
import commands.CdCommand;
import commands.TypeCommand;
import utils.Tokenizer;

public class Main {
    private static File pwd = new File(System.getProperty("user.dir"));
    private static final String[] BUILTIN_COMMANDS = {"echo", "exit"};

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("$ ");
            StringBuilder input = new StringBuilder();
            boolean shouldEcho = true;

            while (true) {
                int ch = System.in.read();
                if (ch == '\n') { // Enter key
                    System.out.println(); // New line after input
                    break;
                } else if (ch == '\t') { // Tab key for autocomplete
                    String partial = input.toString().trim();
                    String completed = autocomplete(partial);
                    if (!completed.equals(partial)) {
                        clearCurrentLine();
                        input.setLength(0);
                        input.append(completed).append(" ");
                        System.out.print("$ " + input);
                    }
                } else if (ch >= 32 && ch < 127) { // Printable characters
                    input.append((char) ch);
                    if (shouldEcho) {
                        System.out.print((char) ch);
                    }
                }
            }

            String commandLine = input.toString().trim();
            if (commandLine.isEmpty()) continue;

            List<String> tokens = Tokenizer.tokenizeInput(commandLine);
            String command = tokens.getFirst();
            String[] commandArgs = tokens.subList(1, tokens.size()).toArray(new String[0]);

            if ("exit 0".equals(commandLine)) {
                System.exit(0);
            }

            switch (command) {
                case "echo" -> EchoCommand.handle(command, commandArgs);
                case "pwd" -> System.out.println(pwd.getAbsolutePath());
                case "cd" -> pwd = CdCommand.handle(commandArgs, pwd);
                case "type" -> TypeCommand.handle(commandArgs);
                default -> CommandExecutor.execute(command, commandArgs);
            }
        }
    }

    private static String autocomplete(String input) {
        if (input.isEmpty()) return input;
        for (String cmd : BUILTIN_COMMANDS) {
            if (cmd.startsWith(input)) {
                return cmd;
            }
        }
        return input;
    }

    private static void clearCurrentLine() {
        System.out.print("\r\033[K"); // Clear the line using ANSI escape sequence
    }
}