package com.dix.codec.bkv;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BKVTest {
    @Test
    void pack() throws IOException {
        BKV bkv = new BKV();
        bkv.add(new KV(0x02, "Hello, world".getBytes()));
        bkv.add(new KV(0x02, new byte[]{ 0x03, 0x04, 0x05 }));
        bkv.add(new KV("dd", "012".getBytes()));
        bkv.add(new KV(99, new byte[]{ 0x03, 0x04, 0x05 }));
        assertEquals("0E010248656C6C6F2C20776F726C6405010203040506826464303132050163030405", CodecUtil.bytesToHex(bkv.pack()));
    }

    @Test
    void unpack() {
        UnpackBKVResult unpackBKVResult = BKV.unpack(CodecUtil.hexToBytes("0E010248656C6C6F2C20776F726C6405010203040506826464303132050163030405"));
        assertNotNull(unpackBKVResult);
        assertEquals(0, unpackBKVResult.getRemainingBuffer().length);

        BKV bkv = unpackBKVResult.getBKV();
        bkv.dump();

        assertTrue(bkv.containsKey(2));
        assertTrue(bkv.containsKey(99));
        assertFalse(bkv.containsKey(2.0));

        assertEquals("Hello, world", bkv.getByIndex(0).getStringValue());
        assertEquals(2, bkv.getByIndex(0).getNumberKey());

        assertEquals("030405", CodecUtil.bytesToHex(bkv.getByIndex(1).getValue()));
        assertEquals(2, bkv.getByIndex(1).getNumberKey());

        assertEquals("012", bkv.getByIndex(2).getStringValue());
        assertEquals("dd", bkv.getByIndex(2).getStringKey());

        assertEquals("030405", CodecUtil.bytesToHex(bkv.getByIndex(3).getValue()));
        assertEquals(99, bkv.getByIndex(3).getNumberKey());
    }
}