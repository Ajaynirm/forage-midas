package com.jpmc.midascore.Consumer;

import java.util.*;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

        // after validating and before saving transaction
        RestTemplate restTemplate = new RestTemplate();
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,   // Spring auto-converts Transaction -> JSON
                Incentive.class
        );

        if(incentive==null) throw new RuntimeException("Invalid Incentive");
        System.out.println("Incentive got is "+incentive);
        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount()+incentive.getAmount());

        // Persist updates
        userRepo.save(sender);
        userRepo.save(receiver);

        // Save transaction record
        TransactionRecord t1 = transactionRepo.save(
                new TransactionRecord(sender, receiver, transaction.getAmount(), incentive.getAmount()));
        System.out.println("Saved transaction: " + t1);

        receivedTransactions.add(transaction);
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }
}





