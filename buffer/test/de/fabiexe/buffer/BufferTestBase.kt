package de.fabiexe.buffer

import kotlin.test.*

abstract class BufferTestBase {
    abstract fun createBuffer(capacity: Int = 0): Buffer

    @Test
    fun testBasicTypes() {
        val buffer = createBuffer()
        val byteVal: Byte = 10
        val boolVal = true
        val intVal = 123456
        val longVal = 123456789012345L
        val floatVal = 1.23f
        val doubleVal = 4.56
        val stringVal = "Hello World \uD83D\uDE00"

        buffer.writeByte(byteVal)
        buffer.writeBoolean(boolVal)
        buffer.writeInt(intVal)
        buffer.writeLong(longVal)
        buffer.writeFloat(floatVal)
        buffer.writeDouble(doubleVal)
        buffer.writeString(stringVal)

        buffer.resetPosition()

        assertEquals(byteVal, buffer.readByte())
        assertEquals(boolVal, buffer.readBoolean())
        assertEquals(intVal, buffer.readInt())
        assertEquals(longVal, buffer.readLong())
        assertEquals(floatVal, buffer.readFloat(), 0.00001f)
        assertEquals(doubleVal, buffer.readDouble(), 0.00001)
        assertEquals(stringVal, buffer.readString())
    }

    @Test
    fun testIntValues() {
        val buffer = createBuffer()
        val values = listOf(0, 1, -1, 100, -100, Int.MAX_VALUE, Int.MIN_VALUE)
        for (v in values) buffer.writeInt(v)
        buffer.resetPosition()
        for (v in values) assertEquals(v, buffer.readInt(), "Failed for value $v")
    }

    @Test
    fun testLongValues() {
        val buffer = createBuffer()
        val values = listOf(0L, 1L, -1L, 100L, -100L, Long.MAX_VALUE, Long.MIN_VALUE)
        for (v in values) buffer.writeLong(v)
        buffer.resetPosition()
        for (v in values) assertEquals(v, buffer.readLong(), "Failed for value $v")
    }

    @Test
    fun testStringValues() {
        val buffer = createBuffer()
        val texts = listOf("", "a", "abc", "Hello World", "Special characters: öäüß", "Emoji: \uD83D\uDE00")
        for (t in texts) buffer.writeString(t)
        buffer.resetPosition()
        for (t in texts) assertEquals(t, buffer.readString())
    }

    @Test
    fun testWriteReadBytes() {
        val buffer = createBuffer()
        val data = byteArrayOf(1, 2, 3, 4, 5)
        buffer.writeBytes(data)
        buffer.resetPosition()
        val readData = buffer.readBytes(data.size)
        assertTrue(data.contentEquals(readData))
    }

    @Test
    fun testFlip() {
        val buffer = createBuffer()
        buffer.writeInt(123)
        buffer.writeInt(456)
        
        val sizeBeforeFlip = buffer.size
        buffer.flip()
        
        assertEquals(sizeBeforeFlip, buffer.capacity)
        assertEquals(123, buffer.readInt())
        assertEquals(456, buffer.readInt())
        assertFailsWith<IndexOutOfBoundsException> { buffer.readByte() }
    }

    @Test
    fun testResize() {
        val buffer = createBuffer(100)
        buffer.writeByte(1)
        
        buffer.resize()
        // For BitBuffer, capacity is in bits. For ByteBuffer, capacity is in bytes.
        // But both should have capacity == size after resize.
        assertEquals(buffer.size, buffer.capacity)
    }
}
