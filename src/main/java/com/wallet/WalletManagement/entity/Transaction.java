package com.wallet.WalletManagement.entity;

import com.wallet.WalletManagement.entity.enums.TransactionStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "fromUserId")
    Long fromUserId;

    @Column(nullable = false, name = "toUserId")
    Long toUserId;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    TransactionStatus status = TransactionStatus.INPROGRESS;

    @Column(nullable = false, name = "amount")
    Double amount;

    @Column(nullable = false, name = "createdDate")
    Date createdDate;

    @Column(nullable = false, name = "lastUpdatedDate")
    Date lastUpdatedDate;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @PrePersist
        //Auto Computing the value
    void beforeInsert() {
        this.createdDate = new Date();
        this.lastUpdatedDate = new Date();
    }

    @PreUpdate
    void beforeUpdate() {
        this.lastUpdatedDate = new Date();
    }
}
