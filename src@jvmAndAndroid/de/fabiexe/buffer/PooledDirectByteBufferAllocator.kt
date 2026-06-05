package de.fabiexe.buffer

import java.lang.ref.Cleaner
import java.nio.ByteBuffer
import java.util.*

class PooledDirectByteBufferAllocator(initialCount: Int, initialSize: Int) : ByteContainerAllocator {
    private val buffers = mutableSetOf<ByteBuffer>()
    private val allocatedSections = IdentityHashMap<ByteBuffer, MutableMap<Int, PooledDirectByteBufferContainer.Section>>()

    init {
        repeat(initialCount) {
            val buffer = ByteBuffer.allocateDirect(initialSize)
            buffers += buffer
            allocatedSections[buffer] = mutableMapOf()
        }
    }

    override fun allocate(size: Int): ByteContainer {
        var section: PooledDirectByteBufferContainer.Section? = null

        Synchronizer.synchronize(buffers) {
            for (buffer in buffers) {
                val sections = allocatedSections[buffer]!!
                var offset = 0
                while (offset < buffer.capacity()) {
                    val allocatedSection = sections.keys.find { it in offset until offset + size }
                    if (allocatedSection == null) {
                        section = PooledDirectByteBufferContainer.Section(buffer, offset, size, false)
                        sections[offset] = section!!
                        break
                    } else {
                        offset = allocatedSection + sections[allocatedSection]!!.size
                    }
                    offset += size
                }
                if (section != null) {
                    break
                }
            }
            if (section == null) {
                val buffer = ByteBuffer.allocateDirect(size)
                section = PooledDirectByteBufferContainer.Section(buffer, 0, size, true)
                buffers += buffer
                allocatedSections[buffer] = mutableMapOf(0 to section)
            }
        }

        val container = PooledDirectByteBufferContainer(section!!)

        cleaner.register(container) {
            Synchronizer.synchronize(buffers) {
                val sections = allocatedSections[section.buffer]!!
                sections -= section.offset
                if (sections.isEmpty() && section.mayDelete) {
                    buffers -= section.buffer
                    allocatedSections -= section.buffer
                }
            }
        }

        return container
    }

    private companion object {
        val cleaner: Cleaner = Cleaner.create()
    }
}