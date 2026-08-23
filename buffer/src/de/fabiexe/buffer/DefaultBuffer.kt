package de.fabiexe.buffer

interface DefaultBuffer : Buffer {
    override fun writeBytes(value: ByteArray) {
        for (b in value) {
            writeByte(b)
        }
    }

    override fun readBytes(length: Int): ByteArray {
        val result = ByteArray(length)
        for (i in 0 until length) {
            result[i] = readByte()
        }
        return result
    }

    override fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    override fun writeFloat(value: Float) {
        writeInt(value.toBits())
    }

    override fun readDouble(): Double {
        return Double.fromBits(readLong())
    }

    override fun writeDouble(value: Double) {
        writeLong(value.toBits())
    }

    override fun writeString(value: String) {
        val bytes: ByteArray = value.encodeToByteArray()
        writeInt(bytes.size)
        writeBytes(bytes)
    }

    override fun readString(): String {
        val length = readInt()
        val bytes = readBytes(length)
        return bytes.decodeToString()
    }
}