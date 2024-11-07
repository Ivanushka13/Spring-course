package ru.davydoff.user;

import ru.davydoff.account.Account;

import java.util.List;

public class User {
    private final int id;
    private final String login;
    private final List<Account> accountList;

    public User(int id, String login, List<Account> accountList) {
        this.accountList = accountList;
        this.login = login;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
