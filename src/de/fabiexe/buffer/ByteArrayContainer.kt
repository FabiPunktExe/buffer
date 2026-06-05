package de.fabiexe.buffer

class ByteArrayContainer(override val bytes: ByteArray) : ByteContainer {
    override val size: Int
        get() = bytes.size

    override fun copyInto(target: ByteContainer, endIndex: Int) {
        if (target !is ByteArrayContainer) {
            throw IllegalArgumentException("Unsupported target type: ${target::class}")
        }
        bytes.copyInto(target.bytes, endIndex = endIndex)
    }

    override fun get(index: Int): Byte {
        return bytes[index]
    }

    override fun set(index: Int, value: Byte) {
        bytes[index] = value
    }
}