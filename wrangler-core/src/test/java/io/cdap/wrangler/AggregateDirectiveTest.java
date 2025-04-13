package io.cdap.wrangler.core;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Row;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregateStatsDirectiveTest {

    @Test
    public void testTotalByteSize() {
        AggregateStatsDirective directive = new AggregateStatsDirective();
        Arguments args = Arguments.builder()
                .add("sourceByteSizeColumn", "byte_column")
                .add("targetTotalSizeColumn", "total_size")
                .build();

        directive.initialize(args);

        Row row1 = new Row();
        row1.add("byte_column", 1024L); // 1 KB in bytes

        Row row2 = new Row();
        row2.add("byte_column", 2048L); // 2 KB in bytes

        ExecutorContext context = Mockito.mock(ExecutorContext.class);

        directive.execute(row1, context);
        directive.execute(row2, context);

        // Simulate finalization (destroy method)
        long finalBytes = directive.convertBytes(directive.totalBytes, "bytes");

        assertEquals(3072, finalBytes); // Total size in bytes: 3 KB
    }

    @Test
    public void testAverageTimeDuration() {
        AggregateStatsDirective directive = new AggregateStatsDirective();
        Arguments args = Arguments.builder()
                .add("sourceTimeDurationColumn", "time_column")
                .add("targetTotalOrAverageTimeColumn", "average_time")
                .addOptional("aggregationType", "average")
                .build();

        directive.initialize(args);

        Row row1 = new Row();
        row1.add("time_column", 1000L); // 1 second in milliseconds

        Row row2 = new Row();
        row2.add("time_column", 2000L); // 2 seconds in milliseconds

        ExecutorContext context = Mockito.mock(ExecutorContext.class);

        directive.execute(row1, context);
        directive.execute(row2, context);

        // Simulate finalization (destroy method)
        long finalTime = directive.convertMilliseconds(directive.totalMilliseconds, "milliseconds");

        assertEquals(1500, finalTime); // Average time in milliseconds: 1.5 seconds
    }

    @Test
    public void testUnitConversion() {
        AggregateStatsDirective directive = new AggregateStatsDirective();
        Arguments args = Arguments.builder()
                .add("sourceByteSizeColumn", "byte_column")
                .add("targetTotalSizeColumn", "total_size")
                .addOptional("sizeUnit", "MB")
                .build();

        directive.initialize(args);

        Row row1 = new Row();
        row1.add("byte_column", 1024 * 1024L); // 1 MB in bytes

        ExecutorContext context = Mockito.mock(ExecutorContext.class);

        directive.execute(row1, context);

        // Simulate finalization (destroy method)
        long finalBytes = directive.convertBytes(directive.totalBytes, "MB");

        assertEquals(1, finalBytes); // Total size in MB: 1 MB
    }
}

    
