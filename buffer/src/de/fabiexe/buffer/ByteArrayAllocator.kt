package de.fabiexe.buffer

object ByteArrayAllocator : ByteContainerAllocator {
    override fun allocate(size: Int): ByteContainer {
        return ByteArrayContainer(ByteArray(size))
    }
}