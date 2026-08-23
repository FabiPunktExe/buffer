package de.fabiexe.buffer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ByteBufferJvmTest {

    @Test
    fun testWithDirectByteBuffer() {
        val buffer = ByteBuffer(DirectByteBufferAllocator)
        buffer.writeInt(123456)
        buffer.writeString("Direct")
        buffer.flip()

        assertEquals(123456, buffer.readInt())
        assertEquals("Direct", buffer.readString())
    }

    @Test
    fun testWithPooledDirectByteBuffer() {
        val allocator = PooledDirectByteBufferAllocator(1, 1024)

        val buffer = ByteBuffer(allocator)
        buffer.writeLong(123456789L)
        buffer.writeBoolean(true)
        buffer.flip()

        assertEquals(123456789L, buffer.readLong())
        assertTrue(buffer.readBoolean())
    }
}
