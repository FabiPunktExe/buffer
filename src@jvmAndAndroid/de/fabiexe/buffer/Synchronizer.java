package de.fabiexe.buffer;

import java.util.function.Supplier;

class Synchronizer {
    public static <T> T synchronize(Object lock, Supplier<T> action) {
        synchronized (lock) {
            return action.get();
        }
    }
}
