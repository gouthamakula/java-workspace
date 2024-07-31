package com.modules.virtual_threads_continuations;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.function.Consumer;

public class GeneratorUsage {

    public static void main(String[] args) {
        var gen = new Generator<String>(source -> {
            source.yield("A");
            source.yield("B");
            source.yield("C");
        });

        while (gen.hasNext()) {
            System.out.println(gen.next());
            System.out.println("Do something");
        }
    }

    private static class Generator<T> {
        private ContinuationScope scope;
        private Continuation cont;
        private Source source;

        public boolean hasNext() {
            return !cont.isDone();
        }

        public T next() {
            var value = source.getValue();
            cont.run();
            return value;
        }

        public class Source {
            private T value;

            public void yield(T value) {
                this.value = value;
                Continuation.yield(scope);
            }

            public T getValue() {
                return this.value;
            }
        }

        public Generator(Consumer<Source> consumer) {
            this.scope = new ContinuationScope("generator");
            this.source = new Source();
            this.cont = new Continuation(scope, () -> consumer.accept(source));
            this.cont.run();
        }
    }

}
