package io.cdap.wrangler.api.parser;

public class ByteSize extends Token {
    private final long bytes;

    public ByteSize(String token) {
        super(token);
        this.bytes = parseBytes(token);
    }

    private long parseBytes(String token) {
        // Example: "10KB", "5MB"
        String unit = token.replaceAll("[0-9]", "").toUpperCase();
        long value = Long.parseLong(token.replaceAll("[^0-9]", ""));
        switch (unit) {
            case "B":
                return value;
            case "KB":
                return value * 1024;
            case "MB":
                return value * 1024 * 1024;
            case "GB":
                return value * 1024 * 1024 * 1024;
            case "TB":
                return value * 1024L * 1024L * 1024L * 1024L;
            default:
                throw new IllegalArgumentException("Invalid byte size unit: " + unit);
        }
    }

    public long getBytes() {
        return bytes;
    }
}
