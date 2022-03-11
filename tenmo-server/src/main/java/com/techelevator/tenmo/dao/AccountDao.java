package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(Long id);

    Account getAccountByUserId(Long id);

    Account createAccount (Account account);

    void deleteAccount (Long id);

    BigDecimal getBalanceByUserId (Long id);

    BigDecimal getBalanceByAccountId (Long id);

    Account addToAccount (Long id, BigDecimal amount);

    Account subtractFromAccount (Long id, BigDecimal amount);

    void sendTeBucks (Long sender, Long receiver, BigDecimal amount);

    void receiveTeBucks (Long receiver, Long sender, BigDecimal amount);


}
