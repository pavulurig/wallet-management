package com.wallet.WalletManagement.service;

import com.wallet.WalletManagement.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User create(User user) throws Exception;

    User login(User user) throws Exception;
}
