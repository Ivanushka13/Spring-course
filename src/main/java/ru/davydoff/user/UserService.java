package ru.davydoff.user;

import org.springframework.stereotype.Service;
import ru.davydoff.account.Account;
import ru.davydoff.account.AccountService;

import java.util.*;

@Service
public class UserService {

    private final Map<Integer, User> usersMap;
    private final Set<String> takenLogins;
    private int idCounter;
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
        usersMap = new HashMap<>();
        takenLogins = new HashSet<>();
        idCounter = 0;
    }

    public User createUser(String login) {
        if(takenLogins.contains(login)) {
            throw new IllegalArgumentException("User with login=%s is already exists"
                    .formatted(login));
        }
        takenLogins.add(login);
        idCounter++;

        User newUser = new User(idCounter, login, new ArrayList<>());
        Account account = accountService.createAccount(newUser);

        newUser.getAccountList().add(account);
        usersMap.put(newUser.getId(), newUser);

        return newUser;
    }

    public Optional<User> findUserById(int userId) {
        return Optional.ofNullable(usersMap.get(userId));
    }

    public List<User> getAllUsers() {
        return usersMap.values().stream().toList();
    }
}
