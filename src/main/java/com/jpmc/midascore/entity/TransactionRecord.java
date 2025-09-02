package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue()
    Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    UserRecord senderId;

    @JoinColumn(nullable = false)
    @ManyToOne
    UserRecord receiverId;

    @Column(nullable = false)
    double amount;

    public TransactionRecord(){}
    public TransactionRecord(UserRecord senderId, UserRecord receiverId,double amount){
        this.senderId=senderId;
        this.receiverId=receiverId;
        this.amount=amount;
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSenderId() {
        return senderId;
    }

    public UserRecord getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                '}';
    }
}
