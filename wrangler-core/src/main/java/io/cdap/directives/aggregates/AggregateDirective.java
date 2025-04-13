package io.cdap.wrangler.core;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotations.PublicEvolving;
import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.api.ExecutorContext;

import java.util.List;

@PublicEvolving
public class AggregateDirective implements Directive {
    private String sourceByteSizeColumn;
    private String sourceTimeDurationColumn;
    private String targetTotalSizeColumn;
    private String targetTotalOrAverageTimeColumn;
    private String sizeUnit = "bytes"; // Default unit
    private String timeUnit = "milliseconds"; // Default unit
    private boolean calculateAverage = false; // Default aggregation type

    private long totalBytes = 0;
    private long totalMilliseconds = 0;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder()
                .define("sourceByteSizeColumn", TokenType.COLUMN_NAME)
                .define("sourceTimeDurationColumn", TokenType.COLUMN_NAME)
                .define("targetTotalSizeColumn", TokenType.COLUMN_NAME)
                .define("targetTotalOrAverageTimeColumn", TokenType.COLUMN_NAME)
                .defineOptional("sizeUnit", TokenType.STRING) // Optional argument for unit conversion
                .defineOptional("timeUnit", TokenType.STRING) // Optional argument for unit conversion
                .defineOptional("aggregationType", TokenType.STRING) // Optional argument for total/average
                .build();
    }

    @Override
    public void initialize(Arguments args) {
        sourceByteSizeColumn = args.value("sourceByteSizeColumn");
        sourceTimeDurationColumn = args.value("sourceTimeDurationColumn");
        targetTotalSizeColumn = args.value("targetTotalSizeColumn");
        targetTotalOrAverageTimeColumn = args.value("targetTotalOrAverageTimeColumn");

        if (args.contains("sizeUnit")) {
            sizeUnit = args.value("sizeUnit").toLowerCase();
        }
        if (args.contains("timeUnit")) {
            timeUnit = args.value("timeUnit").toLowerCase();
        }
        if (args.contains("aggregationType")) {
            calculateAverage = args.value("aggregationType").equalsIgnoreCase("average");
        }
    }

    @Override
    public void execute(Row row, ExecutorContext context) throws Exception {
        Long byteValue = row.getValue(sourceByteSizeColumn);
        Long timeValue = row.getValue(sourceTimeDurationColumn);

        if (byteValue != null) {
            totalBytes += byteValue; // Accumulate byte size
        }
        if (timeValue != null) {
            totalMilliseconds += timeValue; // Accumulate time duration
        }
        rowCount++;
    }

    @Override
    public void destroy() {
        // Finalize the aggregation and convert units if necessary
        long finalBytes = convertBytes(totalBytes, sizeUnit);
        long finalTime = convertMilliseconds(totalMilliseconds, timeUnit);

        if (calculateAverage && rowCount > 0) {
            finalBytes /= rowCount; // Average byte size
            finalTime /= rowCount; // Average time duration
        }

        Row resultRow = new Row();
        resultRow.add(targetTotalSizeColumn, finalBytes);
        resultRow.add(targetTotalOrAverageTimeColumn, finalTime);

        System.out.println(resultRow); // Output the aggregated result (can be replaced with actual pipeline logic)
    }

    private long convertBytes(long bytes, String unit) {
        switch (unit) {
            case "kb":
                return bytes / 1024;
            case "mb":
                return bytes / (1024 * 1024);
            case "gb":
                return bytes / (1024 * 1024 * 1024);
            default:
                return bytes; // Default to bytes
        }
    }

    private long convertMilliseconds(long milliseconds, String unit) {
        switch (unit) {
            case "seconds":
                return milliseconds / 1000;
            case "minutes":
                return milliseconds / (60 * 1000);
            case "hours":
                return milliseconds / (60 * 60 * 1000);
            default:
                return milliseconds; // Default to milliseconds
        }
    }
}
