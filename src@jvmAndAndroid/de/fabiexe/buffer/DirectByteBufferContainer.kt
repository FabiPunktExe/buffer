package de.fabiexe.buffer

import java.nio.ByteBuffer

class DirectByteBufferContainer(val buffer: ByteBuffer) : ByteContainer {
    override val size: Int
        get() = buffer.capacity()

    override val bytes: ByteArray
        get() {
            val byteArray = ByteArray(buffer.capacity())
            buffer.get(0, byteArray)
            return byteArray
        }

    override fun copyInto(target: ByteContainer, endIndex: Int) {
        if (endIndex !in 0..size) {
            throw IndexOutOfBoundsException("End index: $endIndex, Size: $size")
        }

        when (target) {
            is ByteArrayContainer -> {
                buffer.get(target.bytes, 0, endIndex)
            }
            is DirectByteBufferContainer -> {
                target.buffer.put(0, buffer, 0, endIndex)
            }
            is PooledDirectByteBufferContainer -> {
                target.section.buffer.put(target.section.offset, buffer, 0, endIndex)
            }
            else -> throw IllegalArgumentException("Unsupported target type: ${target::class}")
        }
    }

    override fun get(index: Int): Byte {
        return buffer.get(index)
    }

    override fun set(index: Int, value: Byte) {
        buffer.put(index, value)
    }
}