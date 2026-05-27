package de.fabiexe.buffer

internal actual fun arraycopy(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    System.arraycopy(src, srcPos, dest, destPos, length)
}
