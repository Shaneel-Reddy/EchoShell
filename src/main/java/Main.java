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

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Exiting.");
            System.exit(1);
        }

        while (true) {
            System.out.print("$ ");
            StringBuilder inputBuilder = new StringBuilder();
            while (true) {
                char c = (char) console.reader().read();
                if (c == '\t') { // Handle TAB key
                    String partialInput = inputBuilder.toString().trim();
                    if (partialInput.startsWith("ech")) {
                        inputBuilder.setLength(0); // Clear the input
                        inputBuilder.append("echo "); // Autocomplete to "echo "
                        System.out.print("\r$ echo "); // Update the prompt
                    } else if (partialInput.startsWith("exi")) {
                        inputBuilder.setLength(0); // Clear the input
                        inputBuilder.append("exit "); // Autocomplete to "exit "
                        System.out.print("\r$ exit "); // Update the prompt
                    }
                } else if (c == '\n' || c == '\r') { // Handle Enter key
                    break;
                } else {
                    inputBuilder.append(c); // Append the character to the input
                }
            }

            String input = inputBuilder.toString().trim();
            if (input.equals("exit 0")) {
                System.exit(0);
            }

            List<String> tokens = Tokenizer.tokenizeInput(input);
            if (tokens.isEmpty()) {
                continue;
            }

            String command = tokens.getFirst();
            String[] commandArgs = tokens.subList(1, tokens.size()).toArray(new String[0]);

            switch (command) {
                case "echo" -> EchoCommand.handle(command, commandArgs);
                case "pwd" -> System.out.println(pwd.getAbsolutePath());
                case "cd" -> pwd = CdCommand.handle(commandArgs, pwd);
                case "type" -> TypeCommand.handle(commandArgs);
                default -> CommandExecutor.execute(command, commandArgs);
            }
        }
    }
}