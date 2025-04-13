package io.cdap.wrangler.api.parser;

public class TimeDuration extends Token {
    private final long milliseconds;

    public TimeDuration(String token) {
        super(token);
        this.milliseconds = parseMilliseconds(token);
    }

    private long parseMilliseconds(String token) {
        // Example: "150ms", "2h"
        String unit = token.replaceAll("[0-9]", "").toLowerCase();
        long value = Long.parseLong(token.replaceAll("[^0-9]", ""));
        switch (unit) {
            case "ns":
                return value / 1_000_000; // Convert nanoseconds to milliseconds
            case "ms":
                return value; // Milliseconds
            case "s":
                return value * 1000; // Seconds to milliseconds
            case "m":
                return value * 60 * 1000; // Minutes to milliseconds
            case "h":
                return value * 60 * 60 * 1000; // Hours to milliseconds
            case "d":
                return value * 24 * 60 * 60 * 1000; // Days to milliseconds
            default:
                throw new IllegalArgumentException("Invalid time duration unit: " + unit);
        }
    }

    public long getMilliseconds() {
        return milliseconds;
    }
}
