package com.wallet.WalletManagement.controller;

import com.wallet.WalletManagement.entity.User;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user-management/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    ResponseEntity<Map<String, String>> signup(@RequestBody(required = true) User user) throws Exception {
        User createdUser = userService.create(user);
        HashMap<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("message", "Your Account is created");
        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.CREATED);
    }

}
