import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jline.reader.*;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import commands.CommandExecutor;
import commands.EchoCommand;
import commands.CdCommand;
import commands.TypeCommand;
import utils.Tokenizer;
import utils.CommandCompleter;

public class Main {
    private static File pwd = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws IOException {
        // Set up JLine terminal and line reader
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("echo", "exit")) // Add commands for autocompletion
                .build();

        while (true) {
            String input = lineReader.readLine("$ ").trim();

            // Handle exit command
            if (input.startsWith("exit")) {
                int exitCode = 0;
                String[] exitTokens = input.split("\\s+");
                if (exitTokens.length > 1) {
                    try {
                        exitCode = Integer.parseInt(exitTokens[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid exit code. Using default code 0.");
                    }
                }
                System.exit(exitCode);
            }

            // Tokenize input
            List<String> tokens = Tokenizer.tokenizeInput(input);
            if (tokens.isEmpty()) {
                continue;
            }

            // Execute command
            String command = tokens.getFirst().toLowerCase();
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