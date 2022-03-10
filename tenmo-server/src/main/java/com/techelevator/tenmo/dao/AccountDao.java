package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(int id);

    Account getAccountByUserId(int id);

    Account createAccount (Account account);

    void deleteAccount (int id);

    BigDecimal getBalanceByUserId (int id);

}
