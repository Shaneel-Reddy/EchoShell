package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCompleter {
    private static final List<String> BUILT_IN_COMMANDS = Arrays.asList(
            "echo",
            "exit",
            "pwd",
            "cd",
            "type"
    );

    public static String complete(String partial) {
        if (partial.isEmpty()) {
            return "";
        }

        List<String> matches = BUILT_IN_COMMANDS.stream()
                .filter(cmd -> cmd.startsWith(partial))
                .collect(Collectors.toList());

        if (matches.size() == 1) {
            return matches.get(0) + " ";
        }
        return partial;
    }
}