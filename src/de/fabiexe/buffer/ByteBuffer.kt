package de.fabiexe.buffer

import kotlin.math.max

/**
 * A ByteBuffer is a buffer that allows writing and reading bytes, integers, long, booleans, and strings.
 * It automatically expands its capacity as needed.
 */
class ByteBuffer : DefaultBuffer {
    private val containerAllocator: ByteContainerAllocator
    private var byteContainer: ByteContainer

    override val bytes: ByteArray
        get() = byteContainer.bytes

    override var capacity: Int = 0
        private set

    override var size: Int = 0
        private set

    private var position: Int = 0

    constructor() : this(ByteArrayAllocator)

    constructor(containerAllocator: ByteContainerAllocator) : this(containerAllocator, 0)

    constructor(initialCapacity: Int) : this(ByteArrayAllocator, initialCapacity)

    constructor(containerAllocator: ByteContainerAllocator, initialCapacity: Int) {
        this.containerAllocator = containerAllocator
        this.byteContainer = containerAllocator.allocate(initialCapacity)
        this.capacity = initialCapacity
    }

    private fun ensureCapacity(additionalBytes: Int) {
        if (position + additionalBytes > capacity) {
            val newCapacity = max(capacity * 2, position + additionalBytes)
            val newByteContainer = containerAllocator.allocate(newCapacity)
            byteContainer.copyInto(newByteContainer, size)
            byteContainer = newByteContainer
            capacity = newCapacity
        }
    }

    override fun writeByte(value: Byte) {
        ensureCapacity(1)
        byteContainer[position] = value
        position++
        size = max(size, position)
    }

    override fun readByte(): Byte {
        if (position >= size) {
            throw IndexOutOfBoundsException("No more bytes to read")
        }
        val value = byteContainer[position]
        position++
        return value
    }

    override fun writeBoolean(value: Boolean) {
        writeByte(if (value) 1 else 0)
    }

    override fun readBoolean(): Boolean {
        return readByte() != 0.toByte()
    }

    override fun writeInt(value: Int) {
        ensureCapacity(4)
        for (i in 0 until 4) {
            byteContainer[position + i] = ((value shr (8 * (3 - i))) and 0xFF).toByte()
        }
        position += 4
        size = max(size, position)
    }

    override fun readInt(): Int {
        if (position + 4 > size) {
            throw IndexOutOfBoundsException("Not enough bytes to read Int")
        }
        var value = 0
        for (i in 0 until 4) {
            value = value or ((byteContainer[position + i].toInt() and 0xFF) shl (8 * (3 - i)))
        }
        position += 4
        return value
    }

    override fun writeLong(value: Long) {
        ensureCapacity(8)
        for (i in 0 until 8) {
            byteContainer[position + i] = ((value shr (8 * (7 - i))) and 0xFF).toByte()
        }
        position += 8
        size = max(size, position)
    }

    override fun readLong(): Long {
        if (position + 8 > size) {
            throw IndexOutOfBoundsException("Not enough bytes to read Long")
        }
        var value = 0L
        for (i in 0 until 8) {
            value = value or ((byteContainer[position + i].toLong() and 0xFF) shl (8 * (7 - i)))
        }
        position += 8
        return value
    }

    override fun resetPosition() {
        position = 0
    }

    /** Resize the buffer to match the current size  */
    override fun resize() {
        if (size == capacity) {
            return
        }

        capacity = size
        val newByteContainer = containerAllocator.allocate(capacity)
        byteContainer.copyInto(newByteContainer, size)
        byteContainer = newByteContainer
    }

    /** Flip the buffer by resizing and resetting the position  */
    override fun flip() {
        resize()
        resetPosition()
    }
}
