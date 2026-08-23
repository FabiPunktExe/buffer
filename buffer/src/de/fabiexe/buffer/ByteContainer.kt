package de.fabiexe.buffer

interface ByteContainer {
    val size: Int
    val bytes: ByteArray

    /**
     * Copies the bytes from this container into the target container.
     *
     * @param target The target container to copy the bytes into
     * @param endIndex The index in the target container where the copying should end (exclusive)
     * @throws IllegalArgumentException If the target container is not compatible with this container
     */
    fun copyInto(target: ByteContainer, endIndex: Int = size)

    operator fun get(index: Int): Byte
    operator fun set(index: Int, value: Byte)
}