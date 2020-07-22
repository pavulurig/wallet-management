package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.User;
import com.wallet.WalletManagement.exception.InvalidRequestBodyException;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.repository.UserRepository;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User create(User user) throws Exception {
        validateUser(user);
        boolean isAccountAlreadyExisted = isAccountAlreadyExisted(user);
        if (isAccountAlreadyExisted) {
            throw new UnProcessoableException("Account is Already Exists.");
        } else {
            return userRepository.save(user);
        }
    }

    public boolean isAccountAlreadyExisted(User user) {
        String email = user.getEmail();
        Optional<User> existedUser = userRepository.findByEmail(email);
        if (existedUser.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    void validateUser(User user) throws Exception {
        validateMandatoryKeys(user);
        validateEmail(user);
    }

    void validateMandatoryKeys(User user) throws InvalidRequestBodyException {
        if (user.getEmail() == null) {
            throw new InvalidRequestBodyException("Email is Mandatory for Signup.");
        }
        if(user.getPassword() == null){
            throw new InvalidRequestBodyException("Password is Mandatory for Signup.");
        }
    }

    void validateEmail(User user) throws InvalidRequestBodyException {
        String email = user.getEmail();
        if (!isValid(email)) {
            throw new InvalidRequestBodyException("Invalid Email Provided.");
        }
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
