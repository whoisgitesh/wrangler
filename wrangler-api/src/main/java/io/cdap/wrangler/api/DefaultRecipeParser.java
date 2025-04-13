package io.cdap.wrangler.api.impl;

import io.cdap.wrangler.api.ByteSize;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.RecipeParser;
import io.cdap.wrangler.api.RecipeException;
import io.cdap.wrangler.api.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link RecipeParser} with support for BYTE_SIZE and TIME_DURATION tokens.
 */
public class DefaultRecipeParser implements RecipeParser {
    private final List<Directive> directives = new ArrayList<>();

    @Override
    public List<Directive> parse() throws RecipeException {
        // Implementation to parse recipe text into directives
        // (Assume recipe text is provided via constructor or other means)
        return directives;
    }

    @Override
    public void processByteSizeToken(ByteSize token) {
        // Example: Validate or convert to canonical unit
        long bytes = token.getBytes();
        System.out.println("Processed ByteSize: " + bytes + " bytes");
    }

    @Override
    public void processTimeDurationToken(TimeDuration token) {
        // Example: Validate or convert to canonical unit
        long milliseconds = token.getMilliseconds();
        System.out.println("Processed TimeDuration: " + milliseconds + " ms");
    }

    // Helper method to parse individual tokens
    private Token parseToken(String tokenStr) {
        if (tokenStr.matches("\\d+(B|KB|MB|GB|TB)")) {
            ByteSize byteSize = new ByteSize(tokenStr);
            processByteSizeToken(byteSize); // Process via interface method
            return byteSize;
        } else if (tokenStr.matches("\\d+(ns|ms|s|m|h|d)")) {
            TimeDuration timeDuration = new TimeDuration(tokenStr);
            processTimeDurationToken(timeDuration); // Process via interface method
            return timeDuration;
        } else if (tokenStr.matches("\\d+")) {
            return new Token(TokenType.NUMBER, tokenStr);
        } else {
            return new Token(TokenType.STRING, tokenStr);
        }
    }
}
