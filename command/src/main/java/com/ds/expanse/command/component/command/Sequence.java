package com.ds.expanse.command.component.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Sequence<T> implements Iterable {
    final List<T> sequence = new ArrayList<>();

    public Sequence() {
    }

    public void add(T task) {
        sequence.add(task);
    }

    @Override
    public Iterator iterator() {
        return sequence.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        sequence.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return sequence.spliterator();
    }
}
