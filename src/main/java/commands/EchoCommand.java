package commands;

public class EchoCommand {
    public static void handle(String[] args) {
        StringBuilder output = new StringBuilder();
        for (String arg : args) {
            output.append(arg).append(" ");
        }
        System.out.println(output.toString().trim());
    }
}
