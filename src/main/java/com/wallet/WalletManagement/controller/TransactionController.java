package com.wallet.WalletManagement.controller;

import com.wallet.WalletManagement.entity.Transaction;
import com.wallet.WalletManagement.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/transaction-management/")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    ResponseEntity<Object> get(@PathVariable Long id) throws Exception {
        Transaction transaction = transactionService.get(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @RequestMapping(value = "reverse/{id}", method = RequestMethod.POST)
    ResponseEntity<Object> reverseTransaction(@PathVariable Long id) throws Exception {
        Transaction transaction = transactionService.reverse(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }


}
