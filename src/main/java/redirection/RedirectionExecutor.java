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
            pb.redirectOutput(new File(outputFile));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error executing command with redirection: " + e.getMessage());
        }
    }
}