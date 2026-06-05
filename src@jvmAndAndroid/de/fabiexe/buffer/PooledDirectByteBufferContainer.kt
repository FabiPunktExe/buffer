package de.fabiexe.buffer

import java.nio.ByteBuffer

class PooledDirectByteBufferContainer(val section: Section) : ByteContainer {
    override val size: Int
        get() = section.size

    override val bytes: ByteArray
        get() {
            val result = ByteArray(size)
            section.buffer.get(section.offset, result, 0, size)
            return result
        }

    override fun copyInto(target: ByteContainer, endIndex: Int) {
        if (endIndex !in 0..size) {
            throw IndexOutOfBoundsException("End index: $endIndex, Size: $size")
        }

        when (target) {
            is ByteArrayContainer -> {
                section.buffer.get(section.offset, target.bytes, 0, endIndex)
            }
            is DirectByteBufferContainer -> {
                target.buffer.put(0, section.buffer, section.offset, endIndex)
            }
            is PooledDirectByteBufferContainer -> {
                target.section.buffer.put(target.section.offset, section.buffer, section.offset, endIndex)
            }
            else -> throw IllegalArgumentException("Unsupported target type: ${target::class}")
        }
    }

    override fun get(index: Int): Byte {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
        return section.buffer.get(section.offset + index)
    }

    override fun set(index: Int, value: Byte) {
        if (index !in 0 until size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
        section.buffer.put(section.offset + index, value)
    }

    data class Section(val buffer: ByteBuffer, val offset: Int, val size: Int, val mayDelete: Boolean)
}