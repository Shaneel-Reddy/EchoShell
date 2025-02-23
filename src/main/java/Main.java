import java.io.File;
import java.util.List;
import java.util.Scanner;
import commands.CommandExecutor;
import commands.EchoCommand;
import commands.CdCommand;
import commands.TypeCommand;
import utils.Tokenizer;
public class Main {
    private static File pwd = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
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
                case "echo" -> EchoCommand.handle(command,commandArgs);
                case "pwd" -> System.out.println(pwd.getAbsolutePath());
                case "cd" -> pwd = CdCommand.handle(commandArgs, pwd);
                case "type" -> TypeCommand.handle(commandArgs);
                default -> CommandExecutor.execute(command, commandArgs);
            }
        }
    }
}