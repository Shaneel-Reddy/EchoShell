import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage

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
        if (command.equals("echo") || command.equals("exit") || command.equals("type")) {
            return true;
        }
        return false;
    }

}
