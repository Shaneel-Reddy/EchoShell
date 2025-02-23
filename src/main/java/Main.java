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

            while (true) {
                int ch = System.in.read();
                if (ch == -1) {
                    System.exit(0);
                }

                if (ch == '\n') { // Enter key
                    System.out.println();
                    break;
                } else if (ch == '\t') { // Tab key for autocomplete
                    String partial = input.toString().trim();
                    String completed = autocomplete(partial);
                    if (!completed.equals(partial)) {
                        // Clear current line and reprint with completion
                        System.out.print("\r$ ");
                        for (int i = 0; i < input.length(); i++) {
                            System.out.print(" ");
                        }
                        System.out.print("\r$ " + completed + " ");
                        input.setLength(0);
                        input.append(completed).append(" ");
                    }
                } else if (ch >= 32 && ch < 127) { // Printable characters
                    input.append((char) ch);
                    System.out.print((char) ch);
                }
            }

            String commandLine = input.toString().trim();
            if (commandLine.isEmpty()) {
                continue;
            }

            if (commandLine.equals("exit 0")) {
                System.exit(0);
            }

            List<String> tokens = Tokenizer.tokenizeInput(commandLine);
            if (tokens.isEmpty()) {
                continue;
            }

            String command = tokens.getFirst();
            String[] commandArgs = tokens.subList(1, tokens.size()).toArray(new String[0]);

            switch (command) {
                case "echo" -> System.out.println(String.join(" ", commandArgs));
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
}