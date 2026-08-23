package de.fabiexe.buffer.ktor

import de.fabiexe.buffer.SuspendBuffer
import io.ktor.utils.io.*

/**
 * A [SuspendBuffer] that reads primitive values from a ktor [ByteReadChannel].
 *
 * The buffer is stream-based and forward-only. Write operations are not supported.
 *
 * @param channel The channel to read from.
 */
class ByteReadChannelBuffer(val channel: ByteReadChannel) : SuspendBuffer {
    override suspend fun getBytes(): ByteArray {
        throw UnsupportedOperationException("ByteReadChannelBuffer is stream-based and has no backing array")
    }

    override suspend fun getCapacity(): Int {
        throw UnsupportedOperationException("ByteReadChannelBuffer is stream-based and has no fixed capacity")
    }

    override suspend fun getSize(): Int {
        throw UnsupportedOperationException("ByteReadChannelBuffer is stream-based and has no fixed size")
    }

    override suspend fun resetPosition() {
        throw UnsupportedOperationException("ByteReadChannelBuffer is forward-only")
    }

    override suspend fun resize() {
        throw UnsupportedOperationException("ByteReadChannelBuffer is stream-based")
    }

    override suspend fun flip() {
        throw UnsupportedOperationException("ByteReadChannelBuffer is stream-based")
    }

    override suspend fun readByte(): Byte = channel.readByte()
    override suspend fun readBytes(length: Int): ByteArray = channel.readByteArray(length)
    override suspend fun readBoolean(): Boolean = readByte() != 0.toByte()
    override suspend fun readShort(): Short = channel.readShort()
    override suspend fun readInt(): Int = channel.readInt()
    override suspend fun readLong(): Long = channel.readLong()
    override suspend fun readFloat(): Float = channel.readFloat()
    override suspend fun readDouble(): Double = channel.readDouble()

    override suspend fun readString(): String {
        val length = readInt()
        return readBytes(length).decodeToString()
    }

    override suspend fun writeByte(value: Byte) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeBytes(value: ByteArray) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeBoolean(value: Boolean) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeShort(value: Short) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeInt(value: Int) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeLong(value: Long) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeFloat(value: Float) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeDouble(value: Double) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }

    override suspend fun writeString(value: String) {
        throw UnsupportedOperationException("ByteReadChannelBuffer is read-only")
    }
}