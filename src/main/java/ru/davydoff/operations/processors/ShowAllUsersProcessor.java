package ru.davydoff.operations.processors;

import org.springframework.stereotype.Component;
import ru.davydoff.user.User;
import ru.davydoff.operations.CommandOperationType;
import ru.davydoff.operations.OperationCommandProcessor;
import ru.davydoff.user.UserService;

import java.util.List;

@Component
public class ShowAllUsersProcessor implements OperationCommandProcessor {

    private final UserService userService;

    public ShowAllUsersProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        List<User> users = userService.getAllUsers();
        System.out.println("List of users:");
        users.forEach(System.out::println);
    }

    @Override
    public CommandOperationType getOperationType() {
        return CommandOperationType.SHOW_ALL_USERS;
    }
}
