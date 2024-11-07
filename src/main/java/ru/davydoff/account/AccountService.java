package ru.davydoff.account;

import org.springframework.stereotype.Service;
import ru.davydoff.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    private final Map<Integer, Account> accountMap;
    private int idCounter;
    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
        accountMap = new HashMap<>();
        this.idCounter = 0;
    }

    public Account createAccount(User user) {
        idCounter++;
        Account account = new Account(idCounter, user.getId(), accountProperties.getDefaultAmount());
        accountMap.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccountById(int accountId) {
        return Optional.ofNullable(accountMap.get(accountId));
    }

    public List<Account> getAllUserAccounts(int userId) {
        return accountMap.values()
                .stream()
                .filter(account -> account.getUserId() == userId)
                .toList();
    }

    public void depositAccount(int accountId, int moneyToDeposit) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id=%s is not found"
                        .formatted(accountId)));
        if(moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Cannot deposit not positive amount=%s"
                    .formatted(moneyToDeposit));
        }
        account.setMoneyAmount(account.getMoneyAmount() + moneyToDeposit);
    }

    public void withdrawFromAccount(int accountId, int amountToWithdraw) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id=%s is not found"
                        .formatted(accountId)));
        if(amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Cannot withdraw not positive amount=%s"
                    .formatted(amountToWithdraw));
        }
        if(account.getMoneyAmount() < amountToWithdraw) {
            throw new IllegalArgumentException(
                    "Cannot withdraw from account: id=%s, moneyAmount=%s, amountToWithdraw=%s"
                            .formatted(accountId, account.getMoneyAmount(), amountToWithdraw));
        }

        account.setMoneyAmount(account.getMoneyAmount() - amountToWithdraw);
    }

    public Account closeAccount(int accountId) {
        var accountToRemove = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id=%s is not found"
                        .formatted(accountId)));
        List<Account> userAccounts = getAllUserAccounts(accountToRemove.getUserId());
        if(userAccounts.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one user account");
        }
        Account accountToDeposit = userAccounts.stream()
                .filter(account -> account.getId() != accountToRemove.getId())
                .findFirst()
                .orElseThrow();

        accountToDeposit.setMoneyAmount(accountToDeposit.getMoneyAmount() + accountToRemove.getMoneyAmount());

        accountMap.remove(accountId);
        return accountToRemove;
    }

    public void transfer(int fromAccountId, int toAccountId, int amountToTransfer) {
        var accountFrom = findAccountById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id=%s is not found"
                        .formatted(fromAccountId)));
        var accountTo = findAccountById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id=%s is not found"
                        .formatted(toAccountId)));
        if(amountToTransfer <= 0) {
            throw new IllegalArgumentException("Cannot transfer not positive amount=%s"
                    .formatted(amountToTransfer));
        }
        if(accountFrom.getMoneyAmount() < amountToTransfer) {
            throw new IllegalArgumentException(
                    "Cannot transfer from account: id=%s, moneyAmount=%s, amountToTransfer=%s"
                            .formatted(fromAccountId, accountFrom.getMoneyAmount(), amountToTransfer));
        }

        int totalAmountToDeposit = accountFrom.getUserId() != accountTo.getUserId()
                ? (int) (amountToTransfer * (1 - accountProperties.getTransferCommission()))
                : amountToTransfer;

        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amountToTransfer);
        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + totalAmountToDeposit);
    }
}
