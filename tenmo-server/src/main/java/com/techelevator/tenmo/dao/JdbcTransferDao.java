package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "Select * From transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public List<Transfer> getAllTransferByAccountId(int id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "Select * From transfer Where account_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        while (results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(int id) {
        String sql = "Select * From transfer Where transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,id);
        if(result.next()){
            return mapRowToTransfer(result);
        }
        return null;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "Insert Into transfer (transfer_type_id, " +
                "transfer_status_id, account_from, " +
                "account_to, amount) " +
                "Values (?,?,?,?,?) Returning transfer_id;";

        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAccountFrom(),
                transfer.getAccountTo(),transfer.getAmount());
        return getTransferById(newId);
    }



    @Override
    public void deleteTransfer(int id) {
        String sql = "Delete From transfer Where transfer_id = ?;";
        jdbcTemplate.update(sql,id);
    }

    public Transfer mapRowToTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
