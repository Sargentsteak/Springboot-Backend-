package com.Bookstoreproject.repository.jpa;

import com.Bookstoreproject.entity.TransactionAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionAnalyticsDao extends JpaRepository<TransactionAnalytics, Long> {
}
