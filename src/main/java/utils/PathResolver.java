package utils;

import java.io.File;

public class PathResolver {
    public static String findExecutable(String command) {
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
}
