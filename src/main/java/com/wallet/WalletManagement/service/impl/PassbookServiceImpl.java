package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.Passbook;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.repository.PassbookRepository;
import com.wallet.WalletManagement.service.PassbookService;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassbookServiceImpl implements PassbookService {

    @Autowired
    PassbookRepository passbookRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Passbook> getUserPassBook(Long userId) throws Exception {
        Boolean isValidUser = userService.isValidUser(userId);
        if (!isValidUser) {
            throw new UnProcessoableException("Invalid user type is provided");
        }
        List<Passbook> passbook = passbookRepository.findAllByUserId(userId);
        return passbook;
    }

    @Override
    public void printPassBook(Passbook passbook) {
        passbookRepository.save(passbook);
    }
}
