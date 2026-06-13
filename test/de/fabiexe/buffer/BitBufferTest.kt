package de.fabiexe.buffer

import kotlin.test.*

class BitBufferTest : BufferTestBase() {

    override fun createBuffer(capacity: Int): BitBuffer = BitBuffer(capacity)

    @Test
    fun testInitialization() {
        val buffer = BitBuffer()
        assertEquals(0, buffer.size)
        assertEquals(0, buffer.capacity)
        assertEquals(0, buffer.bytes.size)
    }

    @Test
    fun testInitializationWithCapacity() {
        val buffer = BitBuffer(10)
        assertEquals(0, buffer.size)
        assertEquals(10, buffer.capacity)
        assertEquals(2, buffer.bytes.size) // ceil(10/8) = 2
    }

    @Test
    fun testWriteReadBit() {
        val buffer = BitBuffer()
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        
        assertEquals(3, buffer.size)
        
        buffer.resetPosition()
        assertTrue(buffer.readBit())
        assertFalse(buffer.readBit())
        assertTrue(buffer.readBit())
    }

    @Test
    fun testExpand() {
        val buffer = BitBuffer(8)
        repeat(8) { buffer.writeBit(true) }
        
        assertEquals(8, buffer.capacity)
        
        buffer.writeBit(true) // bit 8, triggers expand
        assertTrue(buffer.capacity > 8)
        assertEquals(9, buffer.size)
    }

    @Test
    fun testReadBitOutOfBounds() {
        val buffer = BitBuffer()
        assertFailsWith<IndexOutOfBoundsException> {
            buffer.readBit()
        }
    }

    @Test
    fun testWriteReadBits() {
        val buffer1 = BitBuffer()
        buffer1.writeBit(true)
        buffer1.writeBit(false)
        buffer1.writeBit(true)
        buffer1.resetPosition()

        val buffer2 = BitBuffer()
        buffer2.writeBits(buffer1)
        
        assertEquals(3, buffer2.size)
        buffer2.resetPosition()
        assertTrue(buffer2.readBit())
        assertFalse(buffer2.readBit())
        assertTrue(buffer2.readBit())
    }

    @Test
    fun testInitializationWithByteArray() {
        val bytes = byteArrayOf(0x01, 0x02)
        val buffer = BitBuffer(bytes)
        assertEquals(16, buffer.size)
        assertEquals(16, buffer.capacity)
        assertEquals(bytes.size, buffer.bytes.size)
        assertEquals(bytes[0], buffer.bytes[0])
        assertEquals(bytes[1], buffer.bytes[1])
    }
}
