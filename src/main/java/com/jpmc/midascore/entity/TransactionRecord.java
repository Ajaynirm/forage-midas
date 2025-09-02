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

    @Column
    double incentives;

    public TransactionRecord(){}
    public TransactionRecord(UserRecord senderId, UserRecord receiverId,double amount,double incentives){
        this.senderId=senderId;
        this.receiverId=receiverId;
        this.amount=amount;
        this.incentives=incentives;
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSenderId() {
        return senderId;
    }

    public double getIncentives() {
        return incentives;
    }

    public void setIncentives(double incentives) {
        this.incentives = incentives;
    }

    public UserRecord getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", incentives=" + incentives +
                '}';
    }
}
