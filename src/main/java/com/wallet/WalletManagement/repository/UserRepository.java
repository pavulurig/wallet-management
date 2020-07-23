package com.wallet.WalletManagement.repository;

import com.wallet.WalletManagement.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

}
