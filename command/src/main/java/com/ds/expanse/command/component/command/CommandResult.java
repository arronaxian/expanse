package com.ds.expanse.command.component.command;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;

public class CommandResult implements Result<Boolean> {
    public enum Status { ok(200), created(201), accepted(202), badrequest(400), forbidden(403), error(500), gateway(502);
        @Getter private int code;

        Status(int code) {
            this.code = code;
        }

        public static Status statusOf(int statusCode) {
            switch (statusCode) {
                case 200:
                    return ok;
                case 201:
                    return created;
                case 202:
                    return accepted;
                case 400:
                    return badrequest;
                case 403:
                    return forbidden;
                case 502:
                    return gateway;
                default:
                    return error;
            }
        }
    }

    @Getter private Status statusCode;
    @Getter @Setter private boolean success = true;
    @Getter private String name;

    public CommandResult(String name) {
        this(name, Status.badrequest, false);
    }

    public CommandResult(String name, Status code, boolean success) {
        this.success = success;
        this.statusCode = code;
        this.name = name;
    }

    /**
     * Sets the Outcome for this Result.
     *
     * @param code The code to set.
     * @param success Is this success?
     */
    public void setOutcome(int code, boolean success) {
        this.statusCode =  Status.statusOf(code);
        this.success = success;
    }

    @Override
    public Boolean outcome() {
        return success;
    }

    public final static CommandResult OK(String name) {
        return new CommandResult(name, Status.ok, true);
    }

    public final static CommandResult badRequest(String name) {
        return new CommandResult(name, Status.badrequest, false);
    }

    public final static CommandResult error(String name) {
        return new CommandResult(name, Status.error, false);
    }

    public final static CommandResult gateway(String name) {
        return new CommandResult(name, Status.gateway, false);
    }


    @Override
    public String toString() {
        return name + "[" + isSuccess() + ":" + this.statusCode + "]";
    }
}
