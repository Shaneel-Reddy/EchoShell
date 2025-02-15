package utils;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public static List<String> tokenizeInput(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;
        boolean escapeNext = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escapeNext) {
                currentToken.append(c);
                escapeNext = false;
                continue;
            }

            if (c == '\\') {
                if (inSingleQuotes) {
                    currentToken.append('\\');
                } else if (inDoubleQuotes) {
                    if (i + 1 < input.length()) {
                        char nextChar = input.charAt(i + 1);
                        if (nextChar == '\\' || nextChar == '"' || nextChar == '$') {
                            escapeNext = true;
                        } else {
                            currentToken.append('\\');
                        }
                    } else {
                        currentToken.append('\\');
                    }
                } else {
                    escapeNext = true;
                }
                continue;
            }

            if (c == '\'' && !inDoubleQuotes) {
                inSingleQuotes = !inSingleQuotes;
                continue;
            }

            if (c == '"' && !inSingleQuotes) {
                inDoubleQuotes = !inDoubleQuotes;
                continue;
            }

            if (!inSingleQuotes && !inDoubleQuotes && Character.isWhitespace(c)) {
                if (!currentToken.isEmpty()) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else {
                currentToken.append(c);
            }
        }

        if (!currentToken.isEmpty()) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }
}
