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
    public Account getAccountById(int id) {
        String sql = "Select * From account Where account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,id);
        if (result.next()){
            return mapRowToAccount(result);
        }
        return null;
    }

    @Override
    public Account getAccountByUserId(int id) {
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
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                account.getUserId(), account.getBalance());
        return getAccountByUserId(newId);
    }

    @Override
    public void deleteAccount(int id) {
        String sql = "Delete from account where account_id = ?;";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public BigDecimal getBalanceByUserId(int id) {
        return getAccountByUserId(id).getBalance();
    }

    @Override
    public Account addToAccount(int id, BigDecimal amount) {
        String sql = "Update account set balance = ? where account_id = ?;";

        BigDecimal newB = getBalanceByAccountId(id).add(amount);

        jdbcTemplate.update(sql, newB, id);
        return getAccountById(id);
    }

    @Override
    public Account subtractFromAccount(int id, BigDecimal amount) {
        String sql = "Update account set balance = ? where account_id = ?;";

        BigDecimal newB = getBalanceByAccountId(id).subtract(amount);

        jdbcTemplate.update(sql, newB, id);
        return getAccountById(id);
    }

    @Override
    public void sendTeBucks(int sender, int receiver, BigDecimal amount) {
        subtractFromAccount(sender, amount);
        addToAccount(receiver, amount);
    }

    @Override
    public void receiveTeBucks(int receiver, int sender, BigDecimal amount) {
        subtractFromAccount(sender, amount);
        addToAccount(receiver, amount);
    }

    @Override
    public BigDecimal getBalanceByAccountId(int id) {
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
