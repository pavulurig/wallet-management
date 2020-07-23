package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.Passbook;

import java.util.List;

public interface PassbookService {
    List<Passbook> getUserPassBook(Long userId) throws Exception;
    void printPassBook(Passbook passbook);
}
