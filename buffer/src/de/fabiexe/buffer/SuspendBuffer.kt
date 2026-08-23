package de.fabiexe.buffer

interface SuspendBuffer {
    /**
     * Get the underlying byte array of the buffer. This is where the data is stored.
     *
     * @return The underlying byte array of the buffer
     */
    suspend fun getBytes(): ByteArray

    /**
     * Get the capacity of the buffer.
     *
     * @return The capacity of the buffer
     */
    suspend fun getCapacity(): Int

    /**
     * Get the current size of the buffer.
     *
     * @return The current size of the buffer
     */
    suspend fun getSize(): Int

    /**
     * Resets the position of the buffer to the beginning.
     */
    suspend fun resetPosition()

    /**
     * Resizes the buffer to match its current size.
     */
    suspend fun resize()

    /**
     * Flips the buffer, typically resetting position and potentially resizing.
     */
    suspend fun flip()

    /**
     * Write a single byte to the buffer.
     *
     * @param value The byte value to write to the buffer
     */
    suspend fun writeByte(value: Byte)

    /**
     * Read a single byte from the buffer.
     *
     * @return The byte value read from the buffer
     */
    suspend fun readByte(): Byte

    /**
     * Write a byte array to the buffer.
     *
     * @param value The byte array to write to the buffer
     */
    suspend fun writeBytes(value: ByteArray)

    /**
     * Read a byte array of the specified length from the buffer.
     *
     * @param length The number of bytes to read from the buffer
     * @return The byte array read from the buffer
     */
    suspend fun readBytes(length: Int): ByteArray

    /**
     * Write a boolean value to the buffer.
     *
     * @param value The boolean value to write to the buffer
     */
    suspend fun writeBoolean(value: Boolean)

    /**
     * Read a boolean value from the buffer.
     *
     * @return The boolean value read from the buffer
     */
    suspend fun readBoolean(): Boolean

    /**
     * Write a short value to the buffer.
     *
     * @param value The short value to write to the buffer
     */
    suspend fun writeShort(value: Short)

    /**
     * Read a short value from the buffer.
     *
     * @return The short value read from the buffer
     */
    suspend fun readShort(): Short

    /**
     * Write an integer value to the buffer.
     *
     * @param value The integer value to write to the buffer
     */
    suspend fun writeInt(value: Int)

    /**
     * Read an integer value from the buffer.
     *
     * @return The integer value read from the buffer
     */
    suspend fun readInt(): Int

    /**
     * Write a long value to the buffer.
     *
     * @param value The long value to write to the buffer
     */
    suspend fun writeLong(value: Long)

    /**
     * Read a long value from the buffer.
     *
     * @return The long value read from the buffer
     */
    suspend fun readLong(): Long

    /**
     * Write a float value to the buffer.
     *
     * @param value The float value to write to the buffer
     */
    suspend fun writeFloat(value: Float)

    /**
     * Read a float value from the buffer.
     *
     * @return The float value read from the buffer
     */
    suspend fun readFloat(): Float

    /**
     * Write a double value to the buffer.
     *
     * @param value The double value to write to the buffer
     */
    suspend fun writeDouble(value: Double)

    /**
     * Read a double value from the buffer.
     *
     * @return The double value read from the buffer
     */
    suspend fun readDouble(): Double

    /**
     * Write a string value to the buffer.
     *
     * @param value The string value to write to the buffer
     */
    suspend fun writeString(value: String)

    /**
     * Read a string value from the buffer.
     *
     * @return The string value read from the buffer
     */
    suspend fun readString(): String
}