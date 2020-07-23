package com.wallet.WalletManagement.service.impl;

import com.wallet.WalletManagement.entity.User;
import com.wallet.WalletManagement.exception.InvalidRequestBodyException;
import com.wallet.WalletManagement.exception.UnProcessoableException;
import com.wallet.WalletManagement.repository.UserRepository;
import com.wallet.WalletManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public User login(User user) throws Exception {
        validateLoginEmail(user);
        validateLoginPassword(user);
        Optional<User> exisitedUser = userRepository.findByEmail(user.getEmail());
        if (!exisitedUser.isPresent()) {
            throw new UnProcessoableException("Account Doesnot exist with given email" + user.getEmail());
        } else {
            String givenPassword = user.getPassword();
            String password = exisitedUser.get().getPassword();
            if (givenPassword.equals(password)) {
                return exisitedUser.get();
            } else {
                throw new UnProcessoableException("Invalid Password entered.");
            }
        }
    }

    @Override
    public Boolean isValidUser(Long id) throws Exception {
        Boolean isValidUser = false;
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            isValidUser = true;
        }
        return isValidUser;
    }


    void validateLoginEmail(User user) throws Exception {
        if (user.getEmail() == null) {
            throw new InvalidRequestBodyException("Email is Missing while login.");
        }else{
            validateEmail(user);
        }
    }

    void validateLoginPassword(User user) throws InvalidRequestBodyException {
        if (user.getPassword() == null) {
            throw new InvalidRequestBodyException("Password is missing while login.");
        }
    }

    public boolean isAccountAlreadyExisted(User user) {
        String email = user.getEmail();
        Optional<User> existedUser = userRepository.findByEmail(email);
        return existedUser.isPresent();
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
        if (!isValidEmail(email)) {
            throw new InvalidRequestBodyException("Invalid Email Provided.");
        }
    }

    public static boolean isValidEmail(String email) {
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
