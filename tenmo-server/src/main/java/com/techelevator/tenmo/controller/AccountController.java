package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@NotNull @PathVariable int id){
        return accountDao.getAccountById(id);
    }

    @RequestMapping(path="/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@NotNull @PathVariable int id) {
        return accountDao.getAccountByUserId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountDao.createAccount(account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@NotNull @PathVariable int id) {
        accountDao.deleteAccount(id);
    }

    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@NotNull @PathVariable int id) {
       return accountDao.getBalanceByUserId(id);
    }


}
