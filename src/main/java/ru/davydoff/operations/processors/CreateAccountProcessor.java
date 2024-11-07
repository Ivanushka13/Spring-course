package ru.davydoff.operations.processors;

import org.springframework.stereotype.Component;
import ru.davydoff.account.Account;
import ru.davydoff.user.User;
import ru.davydoff.operations.CommandOperationType;
import ru.davydoff.operations.OperationCommandProcessor;
import ru.davydoff.account.AccountService;
import ru.davydoff.user.UserService;

import java.util.Scanner;

@Component
public class CreateAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final UserService userService;
    private final AccountService accountService;

    public CreateAccountProcessor(
            Scanner scanner,
            UserService userService,
            AccountService accountService
    ) {
        this.scanner = scanner;
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Please enter user id:");
        int userId = Integer.parseInt(scanner.nextLine());
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id=%s not found"
                        .formatted(userId)));
        Account account = accountService.createAccount(user);
        user.getAccountList().add(account);
        System.out.println("Account with id=%s created for user=%s"
                .formatted(account.getId(), user.getLogin()));
    }

    @Override
    public CommandOperationType getOperationType() {
        return CommandOperationType.ACCOUNT_CREATE;
    }
}
