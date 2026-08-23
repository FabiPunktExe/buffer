package de.fabiexe.buffer.ktor

import de.fabiexe.buffer.SuspendBuffer
import io.ktor.utils.io.*

/**
 * A [SuspendBuffer] that writes primitive values to a ktor [ByteWriteChannel].
 *
 * The buffer is stream-based and forward-only. Read operations are not supported.
 *
 * @param channel The channel to write to.
 */
class ByteWriteChannelBuffer(val channel: ByteWriteChannel) : SuspendBuffer {
    override suspend fun getBytes(): ByteArray {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is stream-based and has no backing array")
    }

    override suspend fun getCapacity(): Int {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is stream-based and has no fixed capacity")
    }

    override suspend fun getSize(): Int {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is stream-based and has no fixed size")
    }

    override suspend fun resetPosition() {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is forward-only")
    }

    override suspend fun resize() {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is stream-based")
    }

    override suspend fun flip() {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is stream-based")
    }

    override suspend fun writeByte(value: Byte) = channel.writeByte(value)
    override suspend fun writeBytes(value: ByteArray) = channel.writeByteArray(value)
    override suspend fun writeBoolean(value: Boolean) = writeByte(if (value) 1 else 0)
    override suspend fun writeShort(value: Short) = channel.writeShort(value)
    override suspend fun writeInt(value: Int) = channel.writeInt(value)
    override suspend fun writeLong(value: Long) = channel.writeLong(value)
    override suspend fun writeFloat(value: Float) = channel.writeFloat(value)
    override suspend fun writeDouble(value: Double) = channel.writeDouble(value)

    override suspend fun writeString(value: String) {
        val bytes = value.encodeToByteArray()
        writeInt(bytes.size)
        writeBytes(bytes)
    }

    override suspend fun readByte(): Byte {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readBytes(length: Int): ByteArray {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readBoolean(): Boolean {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readShort(): Short {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readInt(): Int {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readLong(): Long {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readFloat(): Float {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readDouble(): Double {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }

    override suspend fun readString(): String {
        throw UnsupportedOperationException("ByteWriteChannelBuffer is write-only")
    }
}