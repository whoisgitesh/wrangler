package io.cdap.wrangler.core;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Row;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class test {


@Test
public void testAggregateDirective() throws Exception {
    AggregateDirective directive = new AggregateDirective();
    
    Arguments args = Arguments.builder()
            .add("sourceByteSizeColumn", "byte_column")
            .add("sourceTimeDurationColumn", "time_column")
            .add("targetTotalSizeColumn", "total_size")
            .add("targetTotalOrAverageTimeColumn", "total_time")
            .addOptional("sizeUnit", "MB")
            .addOptional("timeUnit", "seconds")
            .addOptional("aggregationType", "total")
            .build();

    directive.initialize(args);

    Row row1 = new Row();
    row1.add("byte_column", 1048576L); // 1 MB in bytes
    row1.add("time_column", 30000L);  // 30 seconds in milliseconds

    Row row2 = new Row();
    row2.add("byte_column", 2097152L); // 2 MB in bytes
    row2.add("time_column", 60000L);   // 60 seconds in milliseconds

    ExecutorContext context = mock(ExecutorContext.class);

    directive.execute(row1, context);
    directive.execute(row2, context);
    
    directive.destroy(); // Finalize aggregation

    assertEquals(3L, directive.convertBytes(directive.totalBytes, "MB")); // Total size in MB: 3 MB
}
}