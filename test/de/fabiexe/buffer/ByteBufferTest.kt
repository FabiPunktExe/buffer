package de.fabiexe.buffer

import kotlin.test.*

class ByteBufferTest : BufferTestBase() {

    override fun createBuffer(capacity: Int): ByteBuffer = ByteBuffer(capacity)

    @Test
    fun testInitialization() {
        val buffer = ByteBuffer()
        assertEquals(0, buffer.size)
        assertEquals(0, buffer.capacity)
    }

    @Test
    fun testInitializationWithCapacity() {
        val buffer = ByteBuffer(10)
        assertEquals(0, buffer.size)
        assertEquals(10, buffer.capacity)
    }

    @Test
    fun testCapacityExpansion() {
        val buffer = ByteBuffer(2) // Start with small capacity
        buffer.writeInt(1) // Requires 4 bytes, triggers expansion
        buffer.resetPosition()
        assertEquals(1, buffer.readInt())
        assertTrue(buffer.capacity >= 4)
    }

    @Test
    fun testReadOutOfBounds() {
        val buffer = ByteBuffer()
        assertFailsWith<IndexOutOfBoundsException> {
            buffer.readByte()
        }
    }

    @Test
    fun testReadIntOutOfBounds() {
        val buffer = ByteBuffer()
        buffer.writeByte(1)
        buffer.resetPosition()
        assertFailsWith<IndexOutOfBoundsException> {
            buffer.readInt()
        }
    }

    @Test
    fun testReadLongOutOfBounds() {
        val buffer = ByteBuffer()
        buffer.writeInt(1)
        buffer.resetPosition()
        assertFailsWith<IndexOutOfBoundsException> {
            buffer.readLong()
        }
    }

    @Test
    fun testInitializationWithByteArray() {
        val bytes = byteArrayOf(0x01, 0x02)
        val buffer = ByteBuffer(bytes)
        assertEquals(2, buffer.size)
        assertEquals(2, buffer.capacity)
        assertEquals(bytes.size, buffer.bytes.size)
        assertEquals(bytes[0], buffer.bytes[0])
        assertEquals(bytes[1], buffer.bytes[1])
    }
}
