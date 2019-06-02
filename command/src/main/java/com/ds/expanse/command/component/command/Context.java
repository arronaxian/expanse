package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.adapter.EngineAdapter;

public interface Context {
    String getCommand();
    EngineAdapter getEngineAdapter();
}
