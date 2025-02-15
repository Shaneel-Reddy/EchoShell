package commands;

import java.io.File;

public class CdCommand {
    public static File handle(String[] args, File currentDir) {
        if (args.length == 0) {
            return currentDir;
        }
        File newDirectory;
        if (args[0].equals("~")) {
            String homeDir = System.getenv("HOME");
            if (homeDir == null) {
                return currentDir;
            }
            newDirectory = new File(homeDir);
        } else {
            newDirectory = new File(args[0]);
            if (!newDirectory.isAbsolute()) {
                newDirectory = new File(currentDir, args[0]);
            }
        }

        try {
            newDirectory = newDirectory.getCanonicalFile();
            if (newDirectory.exists() && newDirectory.isDirectory()) {
                return newDirectory;
            } else {
                System.out.println("cd: " + args[0] + ": No such file or directory");
                return currentDir;
            }
        } catch (Exception e) {
            System.out.println("cd: Error resolving path: " + e.getMessage());
            return currentDir;
        }
    }
}
