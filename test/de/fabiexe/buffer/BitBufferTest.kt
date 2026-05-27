package de.fabiexe.buffer

import kotlin.test.*

class BitBufferTest {

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
        buffer.writeBit(true) // bit 0
        buffer.writeBit(true) // bit 1
        buffer.writeBit(true) // bit 2
        buffer.writeBit(true) // bit 3
        buffer.writeBit(true) // bit 4
        buffer.writeBit(true) // bit 5
        buffer.writeBit(true) // bit 6
        buffer.writeBit(true) // bit 7
        
        assertEquals(8, buffer.capacity)
        
        buffer.writeBit(true) // bit 8, triggers expand
        assertTrue(buffer.capacity > 8)
        assertEquals(9, buffer.size)
    }

    @Test
    fun testWriteReadByte() {
        val buffer = BitBuffer()
        val value: Byte = 0b1010101.toByte()
        buffer.writeByte(value)
        
        assertEquals(8, buffer.size)
        buffer.resetPosition()
        assertEquals(value, buffer.readByte())
    }

    @Test
    fun testWriteReadInt() {
        val buffer = BitBuffer()
        val values = listOf(0, 1, -1, 100, -100, Int.MAX_VALUE, Int.MIN_VALUE)
        
        for (v in values) {
            buffer.writeInt(v)
        }
        
        buffer.resetPosition()
        for (v in values) {
            assertEquals(v, buffer.readInt(), "Failed for value $v")
        }
    }

    @Test
    fun testWriteReadLong() {
        val buffer = BitBuffer()
        val values = listOf(0L, 1L, -1L, 100L, -100L, Long.MAX_VALUE, Long.MIN_VALUE)
        
        for (v in values) {
            buffer.writeLong(v)
        }
        
        buffer.resetPosition()
        for (v in values) {
            assertEquals(v, buffer.readLong(), "Failed for value $v")
        }
    }

    @Test
    fun testWriteReadBoolean() {
        val buffer = BitBuffer()
        buffer.writeBoolean(true)
        buffer.writeBoolean(false)
        
        buffer.resetPosition()
        assertTrue(buffer.readBoolean())
        assertFalse(buffer.readBoolean())
    }

    @Test
    fun testWriteReadString() {
        val buffer = BitBuffer()
        val text = "Hello BitBuffer! \uD83D\uDE00"
        buffer.writeString(text)
        
        buffer.resetPosition()
        assertEquals(text, buffer.readString())
    }

    @Test
    fun testFlip() {
        val buffer = BitBuffer()
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        
        assertEquals(3, buffer.size)
        buffer.flip()
        
        assertEquals(3, buffer.capacity)
        assertTrue(buffer.readBit())
        assertFalse(buffer.readBit())
        assertTrue(buffer.readBit())
        assertFailsWith<IndexOutOfBoundsException> { buffer.readBit() }
    }

    @Test
    fun testResize() {
        val buffer = BitBuffer(100)
        buffer.writeBit(true)
        assertEquals(100, buffer.capacity)
        
        buffer.resize()
        assertEquals(1, buffer.capacity)
        assertEquals(1, buffer.bytes.size)
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
    fun testReadBits() {
        val buffer = BitBuffer()
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.writeBit(true)
        buffer.writeBit(false)
        buffer.resetPosition()
        
        val subBuffer = buffer.readBits(2)
        assertEquals(2, subBuffer.size)
        assertTrue(subBuffer.readBit())
        assertFalse(subBuffer.readBit())
        
        assertTrue(buffer.readBit()) // Check if original buffer position moved
    }
}
