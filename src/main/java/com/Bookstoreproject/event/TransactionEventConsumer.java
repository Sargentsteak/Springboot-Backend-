package com.Bookstoreproject.event;

import com.Bookstoreproject.entity.TransactionAnalytics;
import com.Bookstoreproject.repository.elasticSearch.TransactionAnalyticsElasticDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {
    @Autowired
    TransactionAnalyticsElasticDao tranAnalyticsDao;

    @KafkaListener(
            topics = "transaction.created",
            groupId = "analytics-service-group",
            containerFactory = "transactionEventKafkaListenerContainerFactory"
    )
    public void consumeTransactionCreated(ConsumerRecord<String, TransactionCreatedEvent> record) {
        TransactionCreatedEvent event = record.value();

        TransactionAnalytics doc = new TransactionAnalytics();
        doc.setTransactionId(event.transactionId());
        doc.setWalletId(event.walletId());
        doc.setUserId(event.userId());
        doc.setAmount(event.amount());
        doc.setStatus(event.status());
        doc.setOperationType(event.operationType());
        doc.setCreatedAt(event.createdAt());
        doc.setReferenceId(event.referenceId());

        tranAnalyticsDao.save(doc);

        // Phase 2: index into Elasticsearch (coming soon)
    }
}
