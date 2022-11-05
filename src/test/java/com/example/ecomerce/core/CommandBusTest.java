package com.example.ecomerce.core;


import com.example.ecomerce.core.exception.CommandExecutionException;
import com.example.ecomerce.core.exception.CommandRegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CommandBusTest {

    private CommandBus commandBus;

    @BeforeEach
    public  void setUp() {
       commandBus = CommandBus.create();
    }

    @Test
    public void shouldReturnValueAsSpecifiedByCommandHandler() throws CommandRegistrationException, CommandExecutionException {
        commandBus.register(CommandTest.class, new CommandHandlerTest());
        CommandTest commandTest = new CommandTest("ok");
        String response = commandBus.execute(commandTest);
        assertEquals(response, commandTest.message());
    }


    @Test
    public  void whenCommandAlreadyRegistered_throwCommandRegistrationException() {
        assertThrows(CommandRegistrationException.class, () -> {
            commandBus.register(CommandTest.class, new CommandHandlerTest());
            commandBus.register(CommandTest.class, new CommHandleTestDuplicate());
        });
    }

    @Test
    public void whenCommandUnregistered_thrownCommandExecutionException() {
        assertThrows(CommandExecutionException.class, () -> {
            CommandTest commandTest = new CommandTest("ok");
            commandBus.execute(commandTest);
        });
    }

    private static record CommandTest(String message) implements Command { }

    private  static class CommandHandlerTest implements CommandHandler<CommandTest, String> {
        @Override
        public String handle(CommandTest command) {
            return command.message;
        }
    }

    private  static  class CommHandleTestDuplicate implements CommandHandler<CommandTest, String> {
        @Override
        public  String handle(CommandTest command) {
            return command.message;
        }
    }

}