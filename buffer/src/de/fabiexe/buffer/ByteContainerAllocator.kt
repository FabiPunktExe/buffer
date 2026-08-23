package de.fabiexe.buffer

interface ByteContainerAllocator {
    fun allocate(size: Int): ByteContainer
}