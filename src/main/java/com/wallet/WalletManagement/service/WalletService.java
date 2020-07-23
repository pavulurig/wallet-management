package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.Wallet;
import com.wallet.WalletManagement.entity.WalletEntry;
import com.wallet.WalletManagement.exception.UnProcessoableException;

public interface WalletService {

    WalletEntry addMoneyToWallet(WalletEntry walletEntry) throws Exception;

    Wallet creditMoneyToWallet(Long userId, Double amount);

    Wallet getByUserId(Long userId);

    Wallet debitMoneyFromWallet(Long toUserId, Double amount) throws UnProcessoableException, Exception;
}
