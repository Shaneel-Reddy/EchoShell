import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.trim().equals("exit 0")) {
                System.exit(0);
            }
            if(input.startsWith("echo"))
            {
                System.out.println(input.substring(5));
            }

            else if(input.startsWith("type")) {
                String command = input.substring(5).trim();
                if (command.equals("echo") || command.equals("exit") || command.equals("type")) {
                    System.out.println(command + " is a shell builtin");
                } else {
                    System.out.println(command + ": not found");
                }
            }


            else {
                System.out.println(input + ": command not found");

            if (command.equals("echo")) {
                System.out.println(input.substring(5));
            }
            else if (command.equals("pwd")) {
                System.out.println(System.getProperty("user.dir")); 
            }
            else if (command.equals("type")) {
                handleTypeCommand(commandArgs);
            }
            else {
                executeCommand(command, commandArgs);
            }
        }
    }

    private static void handleCdCommand(String[] args) {
        if (args.length == 0) {
            return;
        }
        File newDirectory;
        if (args[0].equals("~")) {
            String homedir = System.getenv("HOME");
            if (homedir == null) {
                return;
            }
            newDirectory = new File(homedir);
        } else {
            newDirectory = new File(args[0]);
            if (!newDirectory.isAbsolute()) {
                newDirectory = new File(pwd, args[0]);
            }
        }

        try {
            newDirectory = newDirectory.getCanonicalFile();

            if (newDirectory.exists() && newDirectory.isDirectory()) {
                pwd = newDirectory;
            } else {
                System.out.println("cd: " + args[0] + ": No such file or directory");
            }
        } catch (Exception e) {
            System.out.println("cd: Error resolving path: " + e.getMessage());
        }

    }

    private static void executeCommand(String command, String[] args) {
        String executablePath = findExecutable(command);
        if (executablePath == null) {
            System.out.println(command + ": command not found");
            return;
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

    private static void handleTypeCommand(String[] args) {
        if (args.length == 0) {
            System.out.println("type: missing operand");
            return;
        }
        String command = args[0];
        if (isbuiltin(command)) {
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


    private static String findExecutable(String command) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null) {
            return null;
        }
        String[] paths = pathEnv.split(":");
        for (String path : paths) {
            File file = new File(path, command);
            if (file.exists() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    private static boolean isbuiltin(String command) {
        return command.equals("echo") || command.equals("exit") || command.equals("type") || command.equals("pwd");
    }
}
