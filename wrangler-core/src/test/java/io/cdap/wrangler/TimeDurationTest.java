package io.cdap.wrangler.core;

import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeDurationTest {

    @Test
    public void testParseTimeDuration() {
        TimeDuration duration = new TimeDuration("5ms");
        assertEquals(5, duration.getMilliseconds());

        duration = new TimeDuration("2.1s");
        assertEquals(2100, duration.getMilliseconds());

        duration = new TimeDuration("1h");
        assertEquals(3600000, duration.getMilliseconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeDuration() {
        new TimeDuration("10Invalid");
    }
}
