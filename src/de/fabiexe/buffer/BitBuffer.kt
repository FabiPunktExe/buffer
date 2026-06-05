package de.fabiexe.buffer

import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * A BitBuffer is a dynamic buffer that allows writing and reading bits, bytes, integers, and strings.
 * It automatically expands its capacity as needed.
 */
class BitBuffer : DefaultBuffer {
    private val containerAllocator: ByteContainerAllocator
    private var byteContainer: ByteContainer

    override val bytes: ByteArray
        get() = byteContainer.bytes

    var capacity: Int = 0
        private set

    /** The current size of the [BitBuffer] in bits. The size is how many bits are currently written. */
    var size: Int = 0

    private var bitPosition: Int = 0
    private var bytePosition: Int = 0

    /** Create a new BitBuffer with an initial capacity of `0` bits */
    constructor() : this(ByteArrayAllocator)

    /**
     * Create a new BitBuffer with an initial capacity of `0` bits
     *
     * @param containerAllocator The allocator for the underlying byte container
     */
    constructor(containerAllocator: ByteContainerAllocator) : this(containerAllocator, 0)

    /**
     * Create a new BitBuffer with a specified initial capacity in bits
     *
     * @param initialCapacity The initial capacity in bits
     */
    constructor(initialCapacity: Int) : this(ByteArrayAllocator, initialCapacity)

    /**
     * Create a new BitBuffer with a specified initial capacity in bits
     *
     * @param containerAllocator The allocator for the underlying byte container
     * @param initialCapacity The initial capacity in bits
     */
    constructor(containerAllocator: ByteContainerAllocator, initialCapacity: Int) {
        this.containerAllocator = containerAllocator
        this.byteContainer = containerAllocator.allocate(ceil(initialCapacity.toDouble() / Byte.SIZE_BITS).toInt())
        this.capacity = initialCapacity
    }

    /**
     * Expand the buffer's capacity by a specified number of bits.
     *
     * @param additionalCapacity The number of bits to expand the capacity by
     * @throws IllegalArgumentException If the additional capacity is negative
     */
    fun expand(additionalCapacity: Int) {
        if (additionalCapacity == 0) {
            return
        }
        require(additionalCapacity >= 0) { "Additional capacity must be non-negative" }
        capacity += additionalCapacity
        val newByteCapacity = ceil(capacity.toDouble() / Byte.SIZE_BITS).toInt()
        if (newByteCapacity > byteContainer.size) {
            val newByteContainer = containerAllocator.allocate(newByteCapacity)
            byteContainer.copyInto(newByteContainer)
            byteContainer = newByteContainer
        }
    }

    /** Resize the buffer to match the current size  */
    fun resize() {
        if (size == capacity) {
            return
        }

        capacity = size
        val newByteContainer = containerAllocator.allocate(ceil(size.toDouble() / Byte.SIZE_BITS).toInt())
        byteContainer.copyInto(newByteContainer, endIndex = min(byteContainer.size, newByteContainer.size))
        byteContainer = newByteContainer
    }

    /** Reset the position of the buffer to the beginning  */
    fun resetPosition() {
        bitPosition = 0
        bytePosition = 0
    }

    /** Flip the buffer by resizing and resetting the position  */
    fun flip() {
        resize()
        resetPosition()
    }

    /**
     * Write a single bit to the buffer.
     * If the buffer is full, it will expand its capacity.
     * 
     * @param value The bit value (`true` for `1`, `false` for `0`)
     */
    fun writeBit(value: Boolean) {
        if (size >= capacity) {
            expand(capacity / 4 + 1)
        }

        if (value) {
            byteContainer[bytePosition] = (byteContainer[bytePosition].toInt() or (1 shl bitPosition)).toByte()
        } else {
            byteContainer[bytePosition] = (byteContainer[bytePosition].toInt() and (1 shl bitPosition).inv()).toByte()
        }
        size++
        bitPosition++
        if (bitPosition == 8) {
            bitPosition = 0
            bytePosition++
        }
    }

    /**
     * Read a single bit from the buffer
     * 
     * @return The bit value (`true` for `1`, `false` for `0`)
     * @throws IndexOutOfBoundsException If there are no more bits to read
     */
    fun readBit(): Boolean {
        if (bitPosition >= size) {
            throw IndexOutOfBoundsException("No more bits to read")
        }

        val value = (byteContainer[bytePosition].toInt() and (1 shl bitPosition)) != 0
        bitPosition++
        if (bitPosition == 8) {
            bitPosition = 0
            bytePosition++
        }
        return value
    }

    fun writeBits(buffer: BitBuffer) {
        expand(max(0, buffer.size - (capacity - size)))
        repeat(buffer.size) {
            writeBit(buffer.readBit())
        }
    }

    fun readBits(count: Int): BitBuffer {
        if (count < 0 || count > size - bitPosition) {
            throw IndexOutOfBoundsException("Cannot read $count bits from current position")
        }

        val result = BitBuffer(count)
        repeat(count) {
            result.writeBit(readBit())
        }
        result.resetPosition()

        return result
    }

    private fun writeByte(value: Byte, size: Byte) {
        require(size in 1..8) { "Size must be between 1 and 8 bits" }
        for (i in 0 until size) {
            writeBit((value.toInt() and (1 shl i)) != 0)
        }
    }

    private fun readByte(size: Byte): Byte {
        require(size in 1..8) { "Size must be between 1 and 8 bits" }
        var value: Byte = 0
        for (i in 0 until size) {
            if (readBit()) {
                value = (value.toInt() or (1 shl i)).toByte()
            }
        }
        return value
    }

    override fun writeByte(value: Byte) {
        writeByte(value, 8)
    }

    override fun readByte(): Byte {
        return readByte(8)
    }

    override fun writeBoolean(value: Boolean) = writeBit(value)

    override fun readBoolean(): Boolean = readBit()

    override fun writeInt(value: Int) {
        writeBit(value < 0)

        var size: Byte = 0
        if (value >= 0) {
            for (i in 0..30) {
                if ((value and (1 shl i)) != 0) {
                    size = (i + 1).toByte()
                }
            }
        } else {
            for (i in 0..30) {
                if ((value and (1 shl i)) == 0) {
                    size = (i + 1).toByte()
                }
            }
        }
        writeByte(size, 5)

        if (value >= 0) {
            for (i in 0 until size) {
                writeBit((value and (1 shl i)) != 0)
            }
        } else {
            for (i in 0 until size) {
                writeBit((value.inv() and (1 shl i)) != 0)
            }
        }
    }

    override fun readInt(): Int {
        val negative = readBit()
        val size = readByte(5)

        var value = 0
        for (i in 0..<size) {
            if (readBit()) {
                value = value or (1 shl i)
            }
        }

        return if (negative) value.inv() else value
    }

    override fun writeLong(value: Long) {
        writeBit(value < 0)

        var size: Byte = 0
        if (value >= 0) {
            for (i in 0..62) {
                if ((value and (1L shl i)) != 0L) {
                    size = (i + 1).toByte()
                }
            }
        } else {
            for (i in 0..62) {
                if ((value and (1L shl i)) == 0L) {
                    size = (i + 1).toByte()
                }
            }
        }
        writeByte(size, 6)

        if (value >= 0) {
            for (i in 0..<size) {
                writeBit((value and (1L shl i)) != 0L)
            }
        } else {
            for (i in 0..<size) {
                writeBit((value.inv() and (1L shl i)) != 0L)
            }
        }
    }

    override fun readLong(): Long {
        val negative = readBit()
        val size = readByte(6)

        var value: Long = 0
        for (i in 0 until size) {
            if (readBit()) {
                value = value or (1L shl i)
            }
        }

        return if (negative) value.inv() else value
    }
}
