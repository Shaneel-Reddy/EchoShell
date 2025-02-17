package commands;
import redirection.RedirectionExecutor;
import utils.PathResolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class CommandExecutor {
    public static void execute(String command, String[] args) {
        String executablePath = PathResolver.findExecutable(command);
        if (executablePath == null) {
            System.out.println(command + ": command not found");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(">") || args[i].equals("1>") || args[i].equals("2>") || args[i].equals(">>") || args[i].equals("1>>")|| args[i].equals("2>>")) {
                RedirectionExecutor.execute(command, args, i);
                return;
            }
        }
        try {
            List<String> commandList = new ArrayList<>();
            commandList.add(command);
            commandList.addAll(Arrays.asList(args));
            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }
}
