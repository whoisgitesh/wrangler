package io.cdap.wrangler.core;

import io.cdap.wrangler.api.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregationTest {

    @Test
    public void testAggregation() {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row()
                .add("data_transfer_size", 1024L) // 1 KB in bytes
                .add("response_time", 1000L)); // 1 second in milliseconds

        rows.add(new Row()
                .add("data_transfer_size", 2048L) // 2 KB in bytes
                .add("response_time", 2000L)); // 2 seconds in milliseconds

        rows.add(new Row()
                .add("data_transfer_size", 4096L) // 4 KB in bytes
                .add("response_time", 3000L)); // 3 seconds in milliseconds

        // Use TestingRig to execute a recipe against this data
        String[] recipe = new String[] {
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        TestingRig testingRig = new TestingRig();
        List<Row> outputRows = testingRig.execute(recipe, rows);

        // Verify output
        Row outputRow = outputRows.get(0);
        long totalSizeMB = (long) outputRow.getValue("total_size_mb");
        long totalTimeSec = (long) outputRow.getValue("total_time_sec");

        assertEquals(7.5, totalSizeMB, 0.01); // Total size in MB: 7.5 MB
        assertEquals(6, totalTimeSec); // Total time in seconds: 6 seconds
    }
}
