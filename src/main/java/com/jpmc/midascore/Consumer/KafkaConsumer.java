package com.jpmc.midascore.Consumer;

import java.util.*;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    private final List<Transaction> receivedTransactions = new ArrayList<>();

    @KafkaListener(topics = "${general.kafka-topic}",groupId = "midas-group")
    public void listen(Transaction transaction) {
        UserRecord sender=userRepo.findById(transaction.getSenderId()).orElseThrow(()->new RuntimeException("Invalid sender"));
        UserRecord receiver=userRepo.findById(transaction.getRecipientId()).orElseThrow(()-> new RuntimeException("Invalid Receiver"));

        if (transaction.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (sender.getBalance() < transaction.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());

        // Persist updates
        userRepo.save(sender);
        userRepo.save(receiver);

        // Save transaction record
        TransactionRecord t1 = transactionRepo.save(
                new TransactionRecord(sender, receiver, transaction.getAmount()));
        System.out.println("Saved transaction: " + t1);

        receivedTransactions.add(transaction);
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }
}



