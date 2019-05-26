package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.adapter.CommandProcessEngineAdapter;

public interface Context {
    String getCommand();
    CommandProcessEngineAdapter getAdapter();
}
