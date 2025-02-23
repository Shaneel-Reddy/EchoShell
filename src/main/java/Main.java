import java.io.File;
import java.util.List;
import org.jline.reader.*;
import org.jline.reader.impl.LineReaderImpl;
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

    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer((reader, line, candidates) -> {
                    String buffer = line.line();
                    String[] parts = buffer.split("\\s+");

                    if (parts.length == 1) {
                        String completed = CommandCompleter.complete(buffer);
                        if (!completed.equals(buffer)) {
                            candidates.add(new Candidate(completed));
                        }
                    }
                })
                .build();

        while (true) {
            String input;
            try {
                input = lineReader.readLine("$ ").trim();
            } catch (UserInterruptException e) {
                continue;
            } catch (EndOfFileException e) {
                break;
            }

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