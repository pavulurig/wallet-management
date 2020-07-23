package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.Transaction;
import com.wallet.WalletManagement.entity.TransactionEntry;

public interface TransactionService {
    TransactionEntry createTransaction(TransactionEntry transactionEntry,boolean isLoggedIn) throws Exception;
    Transaction get(Long id) throws Exception;
    Transaction reverse(Long id) throws  Exception;
}
