package com.wallet.WalletManagement.repository;

import com.wallet.WalletManagement.entity.Passbook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassbookRepository extends CrudRepository<Passbook, Long> {
    Passbook findByUserId(Long userId);
    List<Passbook> findAllByUserId(Long userId);
}
