package com.modules.virtual_threads_continuations.implementation;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleVirtualThread {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    public static final ContinuationScope scope = new ContinuationScope("virtualThread");
    private Continuation cont;

    private int id;

    public SimpleVirtualThread(Runnable runnable) {
        cont = new Continuation(scope, runnable);
        this.id = COUNTER.get();
    }

    public void run() {
        System.out.println(STR."Virtual Thread \{this.id} is running on \{Thread.currentThread()}");
        cont.run();
    }

}
