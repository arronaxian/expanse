package com.ds.expanse.command.component.command;

import lombok.Getter;

/**
 * Command sequence meta-data.
 */
public class SequenceCommand implements Command {
    @Getter String name;
    @Getter Command command;

    Result result;

    SequenceCommand(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public static SequenceCommand create(String name, Command command) {
        return new SequenceCommand(name, command);
    }

    @Override
    public Result execute(Context context) {
        result = command.execute(context);

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);

        builder.append("[");
        if ( result == null ) {
            builder.append("pending");
        } else {
            builder.append(result.outcome());
        }
        builder.append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        try {
            return this.name.equalsIgnoreCase(object.toString());
        } catch ( Exception e ) {
            return false;
        }
    }
}
