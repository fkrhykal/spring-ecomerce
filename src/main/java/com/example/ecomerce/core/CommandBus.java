package com.example.ecomerce.core;

import com.example.ecomerce.core.exception.CommandExecutionException;
import com.example.ecomerce.core.exception.CommandRegistrationException;

import java.util.HashMap;

public interface CommandBus {

    public <T extends Command, P> void register(Class<T> command, CommandHandler<T, P> handler) throws CommandRegistrationException;
    public <T extends  Command> boolean unregister(Class<T> command);
    public <T extends  Command, P>  P  execute(T command) throws CommandExecutionException;

    public static CommandBus create() {

        return new CommandBus() {

            private  final HashMap<Class<? extends  Command>, CommandHandler<? extends  Command, ?>> handlers = new HashMap<>();

            @Override
            public <T extends Command, P> void register(Class<T> cls, CommandHandler<T, P> handler) throws CommandRegistrationException {
                if (handlers.get(cls) != null) {
                    throw new CommandRegistrationException(cls.getSimpleName() + " already registered");
                }
                handlers.put(cls, handler);
            }

            @Override
            public <T extends Command> boolean unregister(Class<T> cls) {
               return handlers.remove(cls) != null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T extends Command, P> P execute(T command) throws CommandExecutionException {
                CommandHandler<T, P> handler = (CommandHandler<T, P>) handlers.get(command.getClass());
                if (handler != null) {
                    return handler.handle(command);
                }
                throw new CommandExecutionException("Can't find CommandHandler for " + command.getClass().getSimpleName() + ". Please register to CommandBus");
            }
        };
    }
}
