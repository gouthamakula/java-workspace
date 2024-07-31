package com.modules.virtual_threads_continuations.implementation;

public class VirtualThreadMain {
    static final VirtualThreadScheduler SCHEDULER = new VirtualThreadScheduler();

    public static void main(String[] args) {
        new Thread(SCHEDULER::start).start();
        var vt1 = new SimpleVirtualThread(() -> {
            System.out.println("alert1");
            System.out.println("alert2");
            WaitingOperation.perform("Network", 2);
            System.out.println("alert3");
        });
        var vt2 = new SimpleVirtualThread(() -> {
            System.out.println("alert5");
            WaitingOperation.perform("Database", 3);
            System.out.println("alert4");
            System.out.println("alert6");
        });
        SCHEDULER.schedule(vt1);
        SCHEDULER.schedule(vt2);
    }
}
