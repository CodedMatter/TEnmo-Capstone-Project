package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.table.TableRowSorter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferDao transferDao;

    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(){
        return transferDao.getAllTransfers();
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> getAllTransferByAccountId(@NotNull @PathVariable int id){
        return transferDao.getAllTransferByAccountId(id);
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public Transfer getTransferById(@NotNull @PathVariable int id){
        return transferDao.getTransferById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteTransfer(@NotNull @PathVariable int id){
        transferDao.deleteTransfer(id);
    }
}
