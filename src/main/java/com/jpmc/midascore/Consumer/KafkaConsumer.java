package com.jpmc.midascore.Consumer;

import java.util.*;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final List<Transaction> receivedTransactions = new ArrayList<>();

    @KafkaListener(topics = "${general.kafka-topic}",groupId = "midas-group")
    public void listen(Transaction transaction) {
        System.out.println("Received transaction: " + transaction.getAmount());
        receivedTransactions.add(transaction);
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }
}

