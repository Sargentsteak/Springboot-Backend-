package com.Bookstoreproject.repository.jpa;

import com.Bookstoreproject.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {
    // You can define custom query methods here if needed
}
