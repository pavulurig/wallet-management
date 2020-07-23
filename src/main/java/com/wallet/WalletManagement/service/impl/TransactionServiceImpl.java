package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.*;
import com.wallet.WalletManagement.entity.enums.PassbookTransactionType;
import com.wallet.WalletManagement.entity.enums.TransactionStatus;
import com.wallet.WalletManagement.exception.InvalidRequestBodyException;
import com.wallet.WalletManagement.exception.ResourceNotFoundException;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.repository.TransactionRepository;
import com.wallet.WalletManagement.service.PassbookService;
import com.wallet.WalletManagement.service.WalletService;
import com.wallet.WalletManagement.service.TransactionService;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    PassbookService passbookService;


    @Override
    @Transactional
    public TransactionEntry createTransaction(TransactionEntry transactionEntry, boolean isLoggedIn) throws Exception {
        if (isLoggedIn) {
            validateLoggedInTransaction(transactionEntry);
        } else {
            validateTransaction(transactionEntry);
            User user = getUserEntity(transactionEntry);
            User loggedInUser = userService.login(user);
            if (loggedInUser != null) {
                throw new UnProcessoableException("You must login to make transaction.");
            }
            transactionEntry.setFromUserId(loggedInUser.getId());
            transactionEntry.setEmail(loggedInUser.getEmail());
            transactionEntry.setPassword(loggedInUser.getPassword());
        }
        validateToUserId(transactionEntry);
        TransactionEntry savedTransactionEntry = makeTransaction(transactionEntry);
        return savedTransactionEntry;
    }

    @Override
    public Transaction get(Long id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        } else {
            throw new ResourceNotFoundException("Transaction id is invalid");
        }
    }

    @Override
    @Transactional
    public Transaction reverse(Long id) throws Exception {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            Transaction dbTransaction = transaction.get();
            if (dbTransaction.getStatus().toString().equals(TransactionStatus.SUCCESS.toString())) {
                walletService.creditMoneyToWallet(dbTransaction.getFromUserId(), dbTransaction.getAmount());
                walletService.debitMoneyFromWallet(dbTransaction.getToUserId(), dbTransaction.getAmount());
                printReverseTransactionPassBook(dbTransaction);
                TransactionStatus reversedStatus = TransactionStatus.REVERSED;
                dbTransaction.setStatus(reversedStatus);
                return transactionRepository.save(dbTransaction);
            } else {
                throw new UnProcessoableException("Only success Transactions can be reversed.");
            }
        } else {
            throw new ResourceNotFoundException("Transaction id is invalid.");
        }
    }

    void printReverseTransactionPassBook(Transaction dbTransaction) {
        //Print User1 passbook
        Passbook user1 = new Passbook();
        user1.setAmount(dbTransaction.getAmount());
        user1.setTransactionType(PassbookTransactionType.CREDIT);
        user1.setUserId(dbTransaction.getFromUserId());
        passbookService.printPassBook(user1);

        //Print user2 passbook
        Passbook user2 = new Passbook();
        user2.setAmount(dbTransaction.getAmount());
        user2.setTransactionType(PassbookTransactionType.DEBIT);
        user2.setUserId(dbTransaction.getToUserId());
        passbookService.printPassBook(user2);

    }

    TransactionEntry makeTransaction(TransactionEntry transactionEntry) throws Exception {
        Transaction transaction = getTransaction(transactionEntry);
        Transaction inProgressTransaction = transactionRepository.save(transaction);
        Long toUserId = transactionEntry.getToUserId();
        Double amount = transactionEntry.getAmount();
        if (transactionEntry.getToUserId() != transactionEntry.getFromUserId()) {
            //Transferring to another wallet
            validateSufficientMoney(transactionEntry.getFromUserId(), transactionEntry.getAmount());
            Wallet addMoneyToWallet = walletService.creditMoneyToWallet(transaction.getToUserId(), transaction.getAmount());
            walletService.debitMoneyFromWallet(transaction.getFromUserId(), transaction.getAmount());
            TransactionStatus successStatus = TransactionStatus.SUCCESS;
            inProgressTransaction.setStatus(successStatus);
            Transaction successTransaction = transactionRepository.save(inProgressTransaction);
            //Printing passbook should be done in Async: If any issues comes while printing passbook, Transaction will be reverted back(We made this method as @transactional)
            //For now implementing passbook here.
            printPassBook(successTransaction);
            TransactionEntry finalTransactionEntry = getTransactionEntry(successTransaction, addMoneyToWallet);
            return finalTransactionEntry;

        } else {   // Adding money to self
            Wallet addMoneyToWallet = walletService.creditMoneyToWallet(toUserId, amount);
            TransactionStatus successStatus = TransactionStatus.SUCCESS;
            inProgressTransaction.setStatus(successStatus);
            Transaction successTransaction = transactionRepository.save(inProgressTransaction);
            //Printing passbook should be done in Async: If any issues comes while printing passbook, Transaction will be reverted back(We made this method as @transactional)
            //For now implementing passbook here.
            printSelfPassBook(successTransaction);
            TransactionEntry finalTransactionEntry = getTransactionEntry(successTransaction, addMoneyToWallet);
            return finalTransactionEntry;
        }
    }

    void validateSufficientMoney(Long userId,Double amount) throws Exception {
        Wallet wallet = walletService.getByUserId(userId);
        Double balance = wallet.getBalance();
        if(balance.compareTo(amount)<0){
            throw new UnProcessoableException("Insufficient money to make transaction.");
        }
    }

    void printPassBook(Transaction dbTransaction) {
        //Print User1 passbook
        Passbook user1 = new Passbook();
        user1.setAmount(dbTransaction.getAmount());
        user1.setTransactionType(PassbookTransactionType.DEBIT);
        user1.setUserId(dbTransaction.getFromUserId());
        passbookService.printPassBook(user1);

        //Print user2 passbook
        Passbook user2 = new Passbook();
        user2.setAmount(dbTransaction.getAmount());
        user2.setTransactionType(PassbookTransactionType.CREDIT);
        user2.setUserId(dbTransaction.getToUserId());
        passbookService.printPassBook(user2);
    }

    void printSelfPassBook(Transaction dbTransaction) {
        //Print User1 passbook
        Passbook user1 = new Passbook();
        user1.setAmount(dbTransaction.getAmount());
        user1.setTransactionType(PassbookTransactionType.SELF);
        user1.setUserId(dbTransaction.getFromUserId());
        passbookService.printPassBook(user1);

    }


    TransactionEntry getTransactionEntry(Transaction transaction, Wallet wallet) {
        TransactionEntry transactionEntry = new TransactionEntry();
        transactionEntry.setTotalWalletBalance(wallet.getBalance());
        transactionEntry.setId(transaction.getId());
        transactionEntry.setWalletId(wallet.getId());
        transactionEntry.setFromUserId(transaction.getFromUserId());
        transactionEntry.setToUserId(transaction.getToUserId());
        transactionEntry.setCreatedAt(transaction.getCreatedDate());
        transactionEntry.setLastUpdatedAt(transaction.getLastUpdatedDate());
        transactionEntry.setAmount(transaction.getAmount());
        transactionEntry.setTransactionStatus(transaction.getStatus());
        return transactionEntry;
    }

    Transaction getTransaction(TransactionEntry transactionEntry) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionEntry.getAmount());
        transaction.setToUserId(transactionEntry.getToUserId());
        transaction.setFromUserId(transactionEntry.getFromUserId());
        return transaction;
    }

    User getUserEntity(TransactionEntry transactionEntry) {
        User user = new User();
        user.setEmail(transactionEntry.getEmail());
        user.setPassword(transactionEntry.getPassword());
        return user;
    }

    void validateToUserId(TransactionEntry transactionEntry) throws Exception {
        Long toUserId = transactionEntry.getToUserId();
        Boolean isValidUser = userService.isValidUser(toUserId);
        if(!isValidUser){
            throw new UnProcessoableException("Provide valid toUserId to make transaction.");
        }
    }
    void validateLoggedInTransaction(TransactionEntry transactionEntry) throws InvalidRequestBodyException {
        validateMandatoryKeys(transactionEntry);
    }

    void validateMandatoryKeys(TransactionEntry transactionEntry) throws InvalidRequestBodyException {
        if (transactionEntry.getAmount() == null) {
            throw new InvalidRequestBodyException("Amount is mandatory to make transaction.");
        }
        if (transactionEntry.getToUserId() == null) {
            throw new InvalidRequestBodyException("touserId is mandatory to make transaction.");
        }
    }

    void validateTransaction(TransactionEntry transactionEntry) throws InvalidRequestBodyException {
        validateEmailAndPassword(transactionEntry);
        validateMandatoryKeys(transactionEntry);
    }

    void validateEmailAndPassword(TransactionEntry transactionEntry) throws InvalidRequestBodyException {
        if (transactionEntry.getEmail() == null) {
            throw new InvalidRequestBodyException("Email id is mandatory to make transaction.");
        }
        if (transactionEntry.getPassword() == null) {
            throw new InvalidRequestBodyException("password is mandatory to make transaction.");
        }
    }

}
