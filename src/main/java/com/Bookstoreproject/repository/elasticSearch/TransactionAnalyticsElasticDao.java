package com.Bookstoreproject.repository.elasticSearch;

import com.Bookstoreproject.entity.TransactionAnalytics;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TransactionAnalyticsElasticDao extends ElasticsearchRepository<TransactionAnalytics, String> {
    List<TransactionAnalytics> findByUserId(Long userId);
    List<TransactionAnalytics> findByWalletId(Long walletId);
}