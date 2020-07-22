package com.wallet.WalletManagement.repository;

import com.wallet.WalletManagement.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    public Optional<User> findById(Integer id);
    public Optional<User> findByEmail(String  email);

}
