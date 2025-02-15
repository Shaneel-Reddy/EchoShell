package commands;

public class EchoCommand {

    public static void handle(String[] args) {

    public static void handle(String command,String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(">") || args[i].equals("1>") || args[i].equals("2>") || args[i].equals(">>") || args[i].equals("1>>")||args[i].equals("2>>")) {
                RedirectionExecutor.execute(command, args, i);
                return;
            }
        }
        StringBuilder output = new StringBuilder();
        for (String arg : args) {
            output.append(arg).append(" ");
        }
        System.out.println(output.toString().trim());
    }
}
