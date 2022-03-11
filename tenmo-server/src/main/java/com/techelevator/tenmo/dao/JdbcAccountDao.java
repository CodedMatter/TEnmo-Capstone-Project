package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.apache.tomcat.jni.BIOCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account getAccountById(Long id) {
        String sql = "Select * From account Where account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,id);
        if (result.next()){
            return mapRowToAccount(result);
        }
        return null;
    }

    @Override
    public Account getAccountByUserId(Long id) {
        String sql = "Select * From account Where user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,id);
        if(result.next()){
            return mapRowToAccount(result);
        }
        return null;
    }

    @Override
    public Account createAccount(Account account) {
        String sql = "Insert Into account (user_id, balance) " +
                "Values (?,?) Returning account_id";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class,
                account.getUserId(), account.getBalance());
        return getAccountByUserId(newId);
    }

    @Override
    public void deleteAccount(Long id) {
        String sql = "Delete from account where account_id = ?;";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public BigDecimal getBalanceByUserId(Long id) {
        return getAccountByUserId(id).getBalance();
    }

    @Override
    public Account addToAccount(Long id, BigDecimal amount) {
        String sql = "Update account set balance = ? where account_id = ?;";

        BigDecimal newB = getBalanceByAccountId(id).add(amount);

        jdbcTemplate.update(sql, newB, id);
        return getAccountById(id);
    }

    @Override
    public Account subtractFromAccount(Long id, BigDecimal amount) {
        String sql = "Update account set balance = ? where account_id = ?;";

        BigDecimal newB = getBalanceByAccountId(id).subtract(amount);

        jdbcTemplate.update(sql, newB, id);
        return getAccountById(id);
    }

    @Override
    public void sendTeBucks(Long sender, Long receiver, BigDecimal amount) {
        subtractFromAccount(sender, amount);
        addToAccount(receiver, amount);
    }

    @Override
    public void receiveTeBucks(Long receiver, Long sender, BigDecimal amount) {
        subtractFromAccount(sender, amount);
        addToAccount(receiver, amount);
    }

    @Override
    public BigDecimal getBalanceByAccountId(Long id) {
        return getAccountById(id).getBalance();
    }

    public Account mapRowToAccount(SqlRowSet results){
        Account account = new Account();
        account.setId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
