package com.modules.virtual_threads_continuations.implementation;

import jdk.internal.vm.Continuation;

import java.util.Timer;
import java.util.TimerTask;

import static com.modules.virtual_threads_continuations.implementation.VirtualThreadMain.SCHEDULER;
import static com.modules.virtual_threads_continuations.implementation.VirtualThreadScheduler.CURRENT_VIRTUAL_THREAD;

public class WaitingOperation {

    public static void perform(String name, int duration) {
        System.out.println(STR."Waiting for \{name} for \{duration} seconds");
        var virtualThread = CURRENT_VIRTUAL_THREAD.get();
        var timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SCHEDULER.schedule(virtualThread);
                timer.cancel();
            }
        }, duration * 1_000L);
        Continuation.yield(SimpleVirtualThread.scope);
    }

}
