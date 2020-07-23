package com.wallet.WalletManagement.entity;

import com.wallet.WalletManagement.entity.enums.PassbookTransactionType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "passbook")
public class Passbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "userId")
    Long userId;

    @Column(nullable = false, name = "date")
    Date date;

    @Column(nullable = false,  name = "transactionType")
    @Enumerated(EnumType.STRING)
    PassbookTransactionType transactionType;

    @Column(nullable = false,  name = "amount")
    Double amount;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PassbookTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(PassbookTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @PrePersist
        //Auto Computing the value
    void beforeInsert() {
        this.date = new Date();
    }
}
