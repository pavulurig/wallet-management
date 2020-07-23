package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.Transaction;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
}
