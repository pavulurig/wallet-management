package com.wallet.WalletManagement.repository;

import com.wallet.WalletManagement.entity.Passbook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassbookRepository extends CrudRepository<Passbook, Long> {
}
