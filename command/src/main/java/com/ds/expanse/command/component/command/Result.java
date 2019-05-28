package com.ds.expanse.command.component.command;

import java.lang.FunctionalInterface;

/**
 * The Result outcome of the execution of a Command.
 */
@FunctionalInterface
public interface Result<R> {
    /**
     * The outcome of the execution of a Command.
     * @return The specified type.
     */
    R outcome();
}
