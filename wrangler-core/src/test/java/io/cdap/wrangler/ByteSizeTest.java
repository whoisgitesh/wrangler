package io.cdap.wrangler.core;

import io.cdap.wrangler.api.parser.ByteSize;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteSizeTest {

    @Test
    public void testParseByteSize() {
        ByteSize byteSize = new ByteSize("10KB");
        assertEquals(10240, byteSize.getBytes());

        byteSize = new ByteSize("1.5MB");
        assertEquals(1572864, byteSize.getBytes());

        byteSize = new ByteSize("5GB");
        assertEquals(5 * 1024 * 1024 * 1024, byteSize.getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidByteSize() {
        new ByteSize("10Invalid");
    }
}
