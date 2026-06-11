package de.fabiexe.buffer

internal object Synchronizer {
    inline fun <T> synchronize(lock: Any, action: () -> T): T {
        synchronized(lock) {
            return action()
        }
    }
}
