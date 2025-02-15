package commands;

import static utils.PathResolver.findExecutable;

public class TypeCommand {
    public static void handle(String[] args) {
        if (args.length == 0) {
            System.out.println("type: missing operand");
            return;
        }
        String command = args[0];
        if (builtin(command)) {
            System.out.println(command + " is a shell builtin");
        } else {
            String executablePath = findExecutable(command);
            if (executablePath != null) {
                System.out.println(command + " is " + executablePath);
            } else {
                System.out.println(command + ": not found");
            }
        }
    }
    private static boolean builtin(String command) {
        return command.equals("echo") || command.equals("exit") || command.equals("type") || command.equals("pwd");
    }
}
