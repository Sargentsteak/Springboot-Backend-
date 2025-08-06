package com.Bookstoreproject.controller;

import com.Bookstoreproject.beans.TransactionInitiateRequest;
import com.Bookstoreproject.event.TransactionInitiatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orchestrator")
public class TransactionOrchestratorController {

    private final KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate;

    public TransactionOrchestratorController(KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/start")
    public String startTransactionSaga(@RequestBody TransactionInitiateRequest request) {
        String sagaId = UUID.randomUUID().toString();

        TransactionInitiatedEvent event = new TransactionInitiatedEvent(
                sagaId,
                request.userId(),
                request.walletId(),
                request.amount(),
                request.operationType(),
                request.referenceId()
        );

        kafkaTemplate.send("transaction.initiated", request.walletId().toString(), event);

        return "Saga initiated: " + sagaId;
    }
}
