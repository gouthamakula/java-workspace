package com.modules.virtual_threads_continuations;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationUsage {

    public static void main(String[] args) {
        var cont = getContinuations();
        cont.run();
        System.out.println("DO Something");
        cont.run();
    }

    private static Continuation getContinuations() {
        var contScope = new ContinuationScope("contdemo");
        var cont = new Continuation(contScope, () -> {
            System.out.println("A");
            Continuation.yield(contScope);
            System.out.println("B");
            System.out.println("C");
        });
        return cont;
    }

}
