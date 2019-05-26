package com.ds.expanse.command.component;

import com.ds.expanse.command.component.adapter.CommandProcessEngineAdapter;
import com.ds.expanse.command.component.command.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Command processes a Command workflow.  A workflow is a series of requests, effects (mutation) and
 * result.
 */
@Component
public class CommandProcess {
    @Autowired
    private CommandProcessEngineAdapter adapter;

    private final static Logger log = LogManager.getLogger(CommandProcess.class);

    private final static ExecutorService executor = Executors.newFixedThreadPool(5);
    private final static int FUTURE_TIMEOUT_IN_SECONDS = 1000;

    public enum Sequence { none, register, move, view };

    public CommandResult process(Sequence sequence, Context context) {
        log.info("Process sequence start '{}'", sequence.name());

        final StringBuilder sequenceLog = new StringBuilder(sequence.name() + " -> ");
        final List<Command> commandSequence = buildSequence(sequence);
        DefaultContext.fromContext(context).get().setAdapter(adapter);
        CommandResult result = null;
        for ( Command command : commandSequence ) {
            result = execute(context, command);
            sequenceLog.append(result);
            sequenceLog.append(" -> ");

            if ( !result.isSuccess() ) {
                break;
            }
        }

        sequenceLog.append("[");
        sequenceLog.append(result.isSuccess());
        sequenceLog.append("]");

        if ( result.isSuccess() ) {
            log.info("Process sequence completed '{}'", sequenceLog);
        } else {
            log.error("Process sequence failed '{}'", sequenceLog);
        }

        return result;
    }

    public void close() {
        executor.shutdown();
    }

    /**
     * Builds the Commands for the specified Sequence.
     * @param sequence The sequence.
     * @return A list of Commands to process.
     */
    private List<Command> buildSequence(Sequence sequence) {
        switch ( sequence ) {
            case register:
                // Register a Player with a User
                return List.of(CommandEffects.preparePlayer, CommandPlayer.create, CommandPlayer.get, CommandUser.addPlayer);
            case move:
                // Move a Player
                return List.of(CommandPlayer.get, CommandCartograph.heading, CommandEffects.movePlayer, CommandPlayer.set);
            case none:
            default:
                return Collections.EMPTY_LIST;
        }
    }

    /**
     * Executes a single command in the workflow.
     * @param context The context instance containing state.
     * @param command A workflow command
     *
     * @return A CommandResult instance.
     */
    private CommandResult execute(Context context, Command command) {
        CommandResult result = null;
        final Future<CommandResult> future = executor.submit(() -> command.execute(context));
        try {
            result = future.get(FUTURE_TIMEOUT_IN_SECONDS, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("CommandProcess.execute failed ", e);

            result = new CommandResult("CommandProcess.execute", CommandResult.Status.error, false);
        } catch (TimeoutException e) {
            log.error("CommandProcess.execute failed ", e);

            future.cancel(true);

            result = new CommandResult("CommandProcess.timeout", CommandResult.Status.error, false);
        } catch ( Throwable e ) {
            log.error("CommandProcess.execute failed ", e);

            result = new CommandResult("CommandProcess.runtime", CommandResult.Status.error, false);
        }

        // This shouldn't happen, but CYA.
        if ( result == null ) {
            result = new CommandResult("CommandProcess.unknown", CommandResult.Status.error, false);
        }

        return result;
    }

}
