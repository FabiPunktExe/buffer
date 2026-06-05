package de.fabiexe.buffer

import java.nio.ByteBuffer

object DirectByteBufferAllocator : ByteContainerAllocator {
    override fun allocate(size: Int): ByteContainer {
        return DirectByteBufferContainer(ByteBuffer.allocateDirect(size))
    }
}