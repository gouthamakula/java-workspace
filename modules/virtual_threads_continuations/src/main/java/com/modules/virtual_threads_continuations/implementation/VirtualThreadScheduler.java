package com.modules.virtual_threads_continuations.implementation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadScheduler {
    public static final ScopedValue<SimpleVirtualThread> CURRENT_VIRTUAL_THREAD = ScopedValue.newInstance();

    private Queue<SimpleVirtualThread> queue = new ConcurrentLinkedQueue<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void start() {
        while (true) {
            var vt = queue.poll();
            if (vt != null) {
                executorService.submit(() -> ScopedValue.where(CURRENT_VIRTUAL_THREAD, vt).run(vt::run));
            }
        }
    }

    public void schedule(SimpleVirtualThread virtualThread) {
        queue.add(virtualThread);
    }
}
