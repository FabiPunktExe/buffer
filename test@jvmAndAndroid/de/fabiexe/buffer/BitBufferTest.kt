package de.fabiexe.de.fabiexe.buffer

import de.fabiexe.buffer.BitBuffer
import de.fabiexe.buffer.DirectByteBufferAllocator
import de.fabiexe.buffer.PooledDirectByteBufferAllocator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitBufferTest {

    @Test
    fun testWithDirectByteBuffer() {
        val buffer = BitBuffer(DirectByteBufferAllocator)
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        buffer.flip()

        assertTrue(buffer.readBit())
        assertFalse(buffer.readBit())
        assertTrue(buffer.readBit())
    }

    @Test
    fun testWithPooledDirectByteBuffer() {
        val allocator = PooledDirectByteBufferAllocator(1, 1024)

        var buffer = BitBuffer(allocator)
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        buffer.flip()
        assertTrue(buffer.readBit())
        assertFalse(buffer.readBit())
        assertTrue(buffer.readBit())


        buffer = BitBuffer(allocator, 512)
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        buffer.flip()
        assertTrue(buffer.readBit())
        assertFalse(buffer.readBit())
        assertTrue(buffer.readBit())
    }
}
