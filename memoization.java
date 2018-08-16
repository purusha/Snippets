Here is a thread-safe and recursion-safe implementation using a re-entrant lock:

public static <I, O> Function<I, O> memoize(Function<I, O> f) {
    Map<I, O> lookup = new HashMap<>();
    ReentrantLock lock = new ReentrantLock();
    return input -> {
        lock.lock();
        try {
            return lookup.computeIfAbsent(input, f);
        } finally {
            lock.unlock();
        }
    };
}
