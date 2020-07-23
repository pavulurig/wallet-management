package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.Transaction;
import com.wallet.WalletManagement.entity.User;
import com.wallet.WalletManagement.entity.WalletEntry;
import com.wallet.WalletManagement.exception.InvalidRequestBodyException;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.service.AddMoneyToWalletService;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddMoneyToWalletServiceImpl implements AddMoneyToWalletService {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public WalletEntry addMoneyToWallet(WalletEntry walletEntry) throws Exception {
        validateRequestBody(walletEntry);
        User user = getUserDetails(walletEntry);
        User loggedIn = userService.login(user);
        if(loggedIn!=null){
            Transaction newTransaction = createTransaction(walletEntry,user);
        }else{
            throw new UnProcessoableException("You should login to wallet before making transaction.");
        }
    }

    Transaction createTransaction(WalletEntry walletEntry,User user){
        Transaction newTransaction = new Transaction();
        newTransaction.setFromUserId(user.getId());
        newTransaction.setToUserId(user.getId());
        newTransaction.setAmount(walletEntry.getAmount());
        return newTransaction;
    }

    void validateRequestBody(WalletEntry walletEntry) throws InvalidRequestBodyException {
        validateMandatoryKeys(walletEntry);
    }

    void validateMandatoryKeys(WalletEntry walletEntry) throws InvalidRequestBodyException {
        if (walletEntry.getEmail() == null) {
            throw new InvalidRequestBodyException("Email Id is Mandatory to Add Money to wallet.");
        }
        if (walletEntry.getPassword() == null) {
            throw new InvalidRequestBodyException("Password is Mandatory to Add Money to wallet.");
        }
        if (walletEntry.getAmount() == null || walletEntry.getAmount() < 0) {
            throw new InvalidRequestBodyException("Amount should be grater than 0.");
        }
    }

    User getUserDetails(WalletEntry walletEntry) {
        User user = new User();
        user.setEmail(walletEntry.getEmail());
        user.setPassword(walletEntry.getPassword());
        return user;
    }


}
