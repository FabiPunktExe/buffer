package de.fabiexe.buffer.ktor

import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ByteChannelBufferTest {
    @Test
    fun testRoundTrip() = runTest {
        val pipe = ByteChannel()
        val writer = ByteWriteChannelBuffer(pipe)

        val byteVal: Byte = 10
        val byteArrayVal = byteArrayOf(1, 2, 3, 4, 5)
        val boolVal = true
        val shortVal: Short = -42
        val intVal = 123456
        val longVal = 123456789012345L
        val floatVal = 1.23f
        val doubleVal = 4.56
        val stringVal = "Hello World 😀"

        writer.writeByte(byteVal)
        writer.writeBytes(byteArrayVal)
        writer.writeBoolean(boolVal)
        writer.writeShort(shortVal)
        writer.writeInt(intVal)
        writer.writeLong(longVal)
        writer.writeFloat(floatVal)
        writer.writeDouble(doubleVal)
        writer.writeString(stringVal)

        pipe.flush()

        val reader = ByteReadChannelBuffer(pipe)
        assertEquals(byteVal, reader.readByte())
        assertContentEquals(byteArrayVal, reader.readBytes(byteArrayVal.size))
        assertEquals(boolVal, reader.readBoolean())
        assertEquals(shortVal, reader.readShort())
        assertEquals(intVal, reader.readInt())
        assertEquals(longVal, reader.readLong())
        assertEquals(floatVal, reader.readFloat(), 0.00001f)
        assertEquals(doubleVal, reader.readDouble(), 0.00001)
        assertEquals(stringVal, reader.readString())
    }

    @Test
    fun testReadOnly() = runTest {
        val pipe = ByteChannel()
        val reader = ByteReadChannelBuffer(pipe)
        assertFailsWith<UnsupportedOperationException> { reader.writeByte(1) }
        assertFailsWith<UnsupportedOperationException> { reader.writeBytes(byteArrayOf(1, 2, 3)) }
        assertFailsWith<UnsupportedOperationException> { reader.writeBoolean(true) }
        assertFailsWith<UnsupportedOperationException> { reader.writeShort(1) }
        assertFailsWith<UnsupportedOperationException> { reader.writeInt(1) }
        assertFailsWith<UnsupportedOperationException> { reader.writeLong(1) }
        assertFailsWith<UnsupportedOperationException> { reader.writeFloat(1.0f) }
        assertFailsWith<UnsupportedOperationException> { reader.writeDouble(1.0) }
    }

    @Test
    fun testWriteOnly() = runTest {
        val pipe = ByteChannel()
        val writer = ByteWriteChannelBuffer(pipe)
        assertFailsWith<UnsupportedOperationException> { writer.readByte() }
        assertFailsWith<UnsupportedOperationException> { writer.readBytes(3) }
        assertFailsWith<UnsupportedOperationException> { writer.readBoolean() }
        assertFailsWith<UnsupportedOperationException> { writer.readShort() }
        assertFailsWith<UnsupportedOperationException> { writer.readInt() }
        assertFailsWith<UnsupportedOperationException> { writer.readLong() }
        assertFailsWith<UnsupportedOperationException> { writer.readFloat() }
        assertFailsWith<UnsupportedOperationException> { writer.readDouble() }
    }

    @Test
    fun testUnsupportedStreamMethods() = runTest {
        val pipe = ByteChannel()

        val reader = ByteReadChannelBuffer(pipe)
        assertFailsWith<UnsupportedOperationException> { reader.getBytes() }
        assertFailsWith<UnsupportedOperationException> { reader.getCapacity() }
        assertFailsWith<UnsupportedOperationException> { reader.getSize() }
        assertFailsWith<UnsupportedOperationException> { reader.resetPosition() }
        assertFailsWith<UnsupportedOperationException> { reader.resize() }
        assertFailsWith<UnsupportedOperationException> { reader.flip() }
        assertFailsWith<UnsupportedOperationException> { reader.getBytes() }

        val writer = ByteWriteChannelBuffer(pipe)
        assertFailsWith<UnsupportedOperationException> { writer.getBytes() }
        assertFailsWith<UnsupportedOperationException> { writer.getCapacity() }
        assertFailsWith<UnsupportedOperationException> { writer.getSize() }
        assertFailsWith<UnsupportedOperationException> { writer.resetPosition() }
        assertFailsWith<UnsupportedOperationException> { writer.resize() }
        assertFailsWith<UnsupportedOperationException> { writer.flip() }
    }
}