package com.modules.virtual_threads_continuations;

import java.util.concurrent.Executors;

public class ThreadsMain {

	public static void main(String[] args) {
		Thread.ofVirtual().start(() -> System.out.println(Thread.currentThread()));
		Thread.startVirtualThread(() -> System.out.println(Thread.currentThread()));
		try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			executor.submit(() -> System.out.println(Thread.currentThread()));
		}
	}


}
