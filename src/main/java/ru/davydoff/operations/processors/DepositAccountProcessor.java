package ru.davydoff.operations.processors;

import org.springframework.stereotype.Component;
import ru.davydoff.operations.CommandOperationType;
import ru.davydoff.operations.OperationCommandProcessor;
import ru.davydoff.account.AccountService;

import java.util.Scanner;

@Component
public class DepositAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public DepositAccountProcessor(
            Scanner scanner,
            AccountService accountService
    ) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id to deposit: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to deposit: ");
        int amountToDeposit = Integer.parseInt(scanner.nextLine());
        accountService.depositAccount(accountId, amountToDeposit);
        System.out.println("Successfully deposited amount=%s to account=%s"
                .formatted(amountToDeposit, accountId));
    }

    @Override
    public CommandOperationType getOperationType() {
        return CommandOperationType.ACCOUNT_DEPOSIT;
    }
}
