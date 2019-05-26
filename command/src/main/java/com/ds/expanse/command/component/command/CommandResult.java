package com.ds.expanse.command.component.command;

import lombok.Getter;
import lombok.Setter;

public class CommandResult {
    public enum Status { ok(200), created(201), accepted(202), badrequest(400), forbidden(403), error(500);
        private int code;

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
                default:
                    return error;
            }
        }
    }

    @Getter @Setter private String message;
    @Getter @Setter private boolean success = true;
    @Getter private Status code;
    private String name;

    public CommandResult(String name) {
        this(name, Status.badrequest, false);
    }

    public CommandResult(String name, int code, boolean success) {
        this(name, Status.statusOf(code), success);
    }

    public CommandResult(String name, Status code, boolean success) {
        this.success = success;
        this.code = code;
        this.name = name;
    }

    public void setCode(int code) {
        this.code = Status.statusOf(code);
    }

    @Override
    public String toString() {
        return name + "[" + isSuccess() + "]";
    }
}
