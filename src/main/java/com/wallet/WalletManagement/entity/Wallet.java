package com.wallet.WalletManagement.entity;

import com.wallet.WalletManagement.entity.enums.WalletStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, name = "userId")
    Long userId;

    @Column(nullable = true,  name = "balance")
    Double balance;

    @Column(nullable = false,  name = "status")
    @Enumerated(EnumType.STRING)
    WalletStatus status = WalletStatus.ACTIVE;

    @Column(nullable = false, name = "createdDate")
    Date createdDate;

    @Column(nullable = false, name = "lastUpdatedDate")
    Date lastUpdatedDate;

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return createdDate;
    }

    public void setDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public WalletStatus getWalletStatus() {
        return status;
    }

    public void setWalletStatus(WalletStatus walletStatus) {
        this.status = walletStatus;
    }

    @PrePersist    //Auto Computing the value
    void beforeInsert() {
        this.createdDate = new Date();
        this.lastUpdatedDate = new Date();
    }

}
