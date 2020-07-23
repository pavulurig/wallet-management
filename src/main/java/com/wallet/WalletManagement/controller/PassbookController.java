package com.wallet.WalletManagement.controller;

import com.wallet.WalletManagement.entity.Passbook;
import com.wallet.WalletManagement.service.PassbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/passbook-management/")
public class PassbookController {

    @Autowired
    PassbookService passbookService;

    @RequestMapping(value = "{userId}/passbook", method = RequestMethod.GET)
    ResponseEntity<Object> get(@PathVariable Long userId) throws Exception {
        List<Passbook> passbook = passbookService.getUserPassBook(userId);
        return new ResponseEntity<>(passbook, HttpStatus.OK);
    }
}
