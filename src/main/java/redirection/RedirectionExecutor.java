package redirection;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class RedirectionExecutor {
    public static void execute(String command, String[] args, int redirectionIndex) {
        try {

            String outputFile = args[redirectionIndex + 1];

            List<String> commandList = new ArrayList<>();
            commandList.add(command);
            commandList.addAll(Arrays.asList(args).subList(0, redirectionIndex));

            ProcessBuilder pb = new ProcessBuilder(commandList);
<<<<<<< HEAD
            pb.redirectOutput(new File(outputFile));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
=======
            switch (redirectOperator) {
                case ">", "1>" -> {
                    pb.redirectOutput(new File(outputFile));
                    pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                    break;
                }
                case "2>" -> {
                    pb.redirectError(new File(outputFile));
                    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    break;
                }
                default -> {
                    pb.redirectOutput(new File(outputFile));
                    pb.redirectError(new File(outputFile));
                }
            }
>>>>>>> 0925b72 (feat: implement standard error redirection support for commands)
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error executing command with redirection: " + e.getMessage());
        }
    }
}