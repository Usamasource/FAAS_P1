import java.util.concurrent.*;

public class Invoker {
    private final int maxMemory;
    private int usedMemory = 0;
    private final Object memoryLock = new Object();
    private final ExecutorService executor;

    public Invoker(int maxMemory) {
        this.maxMemory = maxMemory;
        this.executor = Executors.newCachedThreadPool();
    }

    public boolean hasAvailableResources(int requiredMemory) {
        synchronized (memoryLock) {
            return (usedMemory + requiredMemory) <= maxMemory;
        }
    }

    public boolean reserveMemory(int requiredMemory) {
        synchronized (memoryLock) {
            if (usedMemory + requiredMemory <= maxMemory) {
                usedMemory += requiredMemory;
                return true;
            }
            return false;
        }
    }

    public void releaseMemory(int amount) {
        synchronized (memoryLock) {
            usedMemory -= amount;
        }
    }

    public int getCurrentMemoryLoad() {
        synchronized (memoryLock) {
            return usedMemory;
        }
    }

    public <T, R> R invoke(Action<T, R> action, T args) throws Exception {
        synchronized (memoryLock) {
            int memoryRequirement = action.getMemoryRequirement();
            if ((usedMemory + memoryRequirement) <= maxMemory) {
                usedMemory += memoryRequirement;
                try {
                    return action.run(args);
                } finally {
                    usedMemory -= memoryRequirement;
                }
            } else {
                throw new RuntimeException("Not enough resources for this action.");
            }
        }
    }

    public <T, R> Future<R> invokeAsync(Action<T, R> action, T args) {
        int memoryRequirement = action.getMemoryRequirement();
        if (!reserveMemory(memoryRequirement)) {
            throw new RuntimeException("Not enough resources to execute the action.");
        }

        return executor.submit(() -> {
            try {
                return action.run(args);
            } finally {
                releaseMemory(memoryRequirement);
            }
        });
    }
}
