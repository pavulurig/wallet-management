package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.WalletEntry;

public interface AddMoneyToWalletService {

    WalletEntry addMoneyToWallet(WalletEntry walletEntry) throws Exception;
}
