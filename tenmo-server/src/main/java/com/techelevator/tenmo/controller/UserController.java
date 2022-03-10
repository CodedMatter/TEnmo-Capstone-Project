package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    @RequestMapping(path = "/username/{id}", method = RequestMethod.GET)
    public int findIdByUsername(String username){
        return userDao.findIdByUsername(username);
    }
}
