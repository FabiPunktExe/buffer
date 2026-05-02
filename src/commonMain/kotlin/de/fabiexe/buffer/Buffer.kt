package de.fabiexe.buffer

interface Buffer {
    /**
     * The underlying byte array of the buffer.
     * This is where the data is stored.
     */
    val bytes: ByteArray

    /**
     * Write a single byte to the buffer.
     *
     * @param value The byte value to write to the buffer
     */
    fun writeByte(value: Byte)

    /**
     * Read a single byte from the buffer.
     *
     * @return The byte value read from the buffer
     */
    fun readByte(): Byte

    /**
     * Write a byte array to the buffer.
     *
     * @param value The byte array to write to the buffer
     */
    fun writeBytes(value: ByteArray)

    /**
     * Read a byte array of the specified length from the buffer.
     *
     * @param length The number of bytes to read from the buffer
     * @return The byte array read from the buffer
     */
    fun readBytes(length: Int): ByteArray

    /**
     * Write a boolean value to the buffer.
     *
     * @receiver value The boolean value to write to the buffer
     */
    fun writeBoolean(value: Boolean)

    /**
     * Read a boolean value from the buffer.
     *
     * @return The boolean value read from the buffer
     */
    fun readBoolean(): Boolean

    /**
     * Write an integer value to the buffer.
     *
     * @receiver value The integer value to write to the buffer
     */
    fun writeInt(value: Int)

    /**
     * Read an integer value from the buffer.
     *
     * @return The integer value read from the buffer
     */
    fun readInt(): Int

    /**
     * Write a long value to the buffer.
     *
     * @receiver value The long value to write to the buffer
     */
    fun writeLong(value: Long)

    /**
     * Read a long value from the buffer.
     *
     * @return The long value read from the buffer
     */
    fun readLong(): Long

    /**
     * Write a float value to the buffer.
     *
     * @receiver value The float value to write to the buffer
     */
    fun writeFloat(value: Float)

    /**
     * Read a float value from the buffer.
     *
     * @return The float value read from the buffer
     */
    fun readFloat(): Float

    /**
     * Write a double value to the buffer.
     *
     * @receiver value The double value to write to the buffer
     */
    fun writeDouble(value: Double)

    /**
     * Read a double value from the buffer.
     *
     * @return The double value read from the buffer
     */
    fun readDouble(): Double

    /**
     * Write a string value to the buffer.
     *
     * @receiver value The string value to write to the buffer
     */
    fun writeString(value: String)

    /**
     * Read a string value from the buffer.
     *
     * @return The string value read from the buffer
     */
    fun readString(): String
}