package com.ds.expanse.command.component.command;

/**
 * Command execution function.
 */
public interface Command {
    CommandResult execute(Context context);
}
