package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.*;
import com.wallet.WalletManagement.exception.InvalidRequestBodyException;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.repository.WalletRepository;
import com.wallet.WalletManagement.service.WalletService;
import com.wallet.WalletManagement.service.TransactionService;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    UserService userService;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionService transactionService;

    @Override
    @Transactional
    public WalletEntry addMoneyToWallet(WalletEntry walletEntry) throws Exception {
        validateRequestBody(walletEntry);
        User user = getUserDetails(walletEntry);
        User loggedIn = userService.login(user);
        if (loggedIn != null) {
            TransactionEntry transactionEntry = createTransactionEntry(walletEntry, loggedIn);
            TransactionEntry savedTransaction = transactionService.createTransaction(transactionEntry, true);
            WalletEntry finalWalletEntry = getWalletEntry(savedTransaction);
            return finalWalletEntry;
        } else {
            throw new UnProcessoableException("You should login to wallet before making transaction.");
        }
    }

    @Override
    public Wallet creditMoneyToWallet(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if(wallet == null){
            Wallet createWallet = new Wallet();
            createWallet.setUserId(userId);
            createWallet.setBalance(amount);
            return walletRepository.save(createWallet);
        }else{
            Double newAmount = wallet.getBalance();
            newAmount = newAmount + amount;
            wallet.setBalance(newAmount);
            return walletRepository.save(wallet);
        }
    }

    @Override
    public Wallet getByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public Wallet debitMoneyFromWallet(Long toUserId, Double amount) throws Exception {
        Wallet wallet = walletRepository.findByUserId(toUserId);
        if (wallet == null) {
            throw new UnProcessoableException("Wallet Doesn't exists for the user");
        } else {
            Double balance = wallet.getBalance();
            balance = balance - amount;
            wallet.setBalance(balance);
            return walletRepository.save(wallet);
        }
    }

    WalletEntry getWalletEntry(TransactionEntry transactionEntry) {
        WalletEntry walletEntry = new WalletEntry();
        walletEntry.setId(transactionEntry.getWalletId());
        walletEntry.setAmount(transactionEntry.getAmount());
        walletEntry.setBalance(transactionEntry.getTotalWalletBalance());
        walletEntry.setTransactionId(transactionEntry.getId());
        walletEntry.setUserId(transactionEntry.getFromUserId());
        walletEntry.setToUserId(transactionEntry.getToUserId());
        return walletEntry;
    }

    TransactionEntry createTransactionEntry(WalletEntry walletEntry, User user) {
        TransactionEntry transactionEntry = new TransactionEntry();
        transactionEntry.setFromUserId(user.getId());
        transactionEntry.setToUserId(walletEntry.getToUserId());  //To Whom we are sending,Can be self transfer also.
        transactionEntry.setAmount(walletEntry.getAmount());
        return transactionEntry;
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
