package com.ds.expanse.command.component.command;

/**
 * Command execution function.
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the Command for the given Context and provides a Result.
     * @param context The context to execute against.
     * @return A Result instance.
     */
    Result execute(Context context);
}
