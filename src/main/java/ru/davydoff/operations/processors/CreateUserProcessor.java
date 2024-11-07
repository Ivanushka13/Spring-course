package ru.davydoff.operations.processors;

import org.springframework.stereotype.Component;
import ru.davydoff.user.User;
import ru.davydoff.operations.CommandOperationType;
import ru.davydoff.operations.OperationCommandProcessor;
import ru.davydoff.user.UserService;

import java.util.Scanner;

@Component
public class CreateUserProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final UserService userService;

    public CreateUserProcessor(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Please enter login:");
        var login = scanner.nextLine();
        User user = userService.createUser(login);
        System.out.println("User successfully created: " + user);
    }

    @Override
    public CommandOperationType getOperationType() {
        return CommandOperationType.USER_CREATE;
    }
}
