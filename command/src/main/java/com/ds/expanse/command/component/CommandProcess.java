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

    public Result process(Sequence sequence, Context context) {
        log.info("Process sequence start '{}'", sequence.name());

        final StringBuilder sequenceLog = new StringBuilder(sequence.name() + " -> ");

        final List<Command> commandSequence = buildSequence(sequence);
        DefaultContext.fromContext(context).get().setAdapter(adapter);
        Result<Boolean> result = null;
        for ( Command command : commandSequence ) {
            result = execute(context, command, sequenceLog);

            if ( !result.outcome() ) {
                break;
            }
        }

        sequenceLog.append(result);

        if ( result.outcome() ) {
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
                return List.of(
                        SequenceCommand.create("CommandEffects.preparePlayer", CommandEffects.preparePlayer),
                        SequenceCommand.create("CommandPlayer.create", CommandPlayer.create),
                        SequenceCommand.create("CommandPlayer.get", CommandPlayer.get),
                        SequenceCommand.create("CommandUser.addPlayer", CommandUser.addPlayer)
                );
            case move:
                // Move a Player
                return List.of(
                        SequenceCommand.create("CommandPlayer.get", CommandPlayer.get),
                        SequenceCommand.create("CommandCartograph.heading", CommandCartograph.heading),
                        SequenceCommand.create("CommandEffects.movePlayer", CommandEffects.movePlayer),
                        SequenceCommand.create("CommandPlayer.set", CommandPlayer.set)
                );
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
    private Result execute(Context context, Command command, StringBuilder sequenceLog) {
        Result result = null;
        final Future<Result> future = executor.submit(() -> command.execute(context));
        try {
            result = future.get(FUTURE_TIMEOUT_IN_SECONDS, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("CommandProcess.execute failed ", e);

            result = CommandResult.badRequest("CommandProcess.execute");
        } catch (TimeoutException e) {
            log.error("CommandProcess.timeout failed - {}", e.getMessage());
            future.cancel(true);

            result = CommandResult.gateway("CommandProcess.timeout");
        } catch ( Throwable e ) {
            log.error("CommandProcess.execute failed ", e);

            result = CommandResult.error("CommandProcess.runtime");
        }

        // This shouldn't happen, but CYA.
        if ( result == null ) {
            result = CommandResult.error("CommandProcess.unknown");
        }

        sequenceLog.append(command);

        sequenceLog.append(" -> ");

        return result;
    }

}
