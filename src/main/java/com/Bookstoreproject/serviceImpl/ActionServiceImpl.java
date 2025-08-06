package com.Bookstoreproject.serviceImpl;

import com.Bookstoreproject.beans.BooksCountResponseBean;
import com.Bookstoreproject.beans.CreateBookRequest;
import com.Bookstoreproject.beans.ResponseBeanBook;
import com.Bookstoreproject.beans.SortedBookResponseBean;
import com.Bookstoreproject.entity.Book;
import com.Bookstoreproject.exceptions.BookNotFoundException;
import com.Bookstoreproject.repository.jpa.BookDao;
import com.Bookstoreproject.service.Actionservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionServiceImpl implements Actionservice {

    private static final Logger logger = LoggerFactory.getLogger(ActionServiceImpl.class);

    @Autowired
    private BookDao bookDao;

    @Override
    public ResponseBeanBook createBook(CreateBookRequest request) {
        logger.info("Received request to create book: {}", request);

        ResponseBeanBook response = new ResponseBeanBook();

        try {
            // Validate request
            if (request.getAuthor() == null || request.getGenre() == null || request.getAvailableCopies() < 0) {
                logger.warn("Invalid book data provided: {}", request);
                response.setStatus("FAILURE");
                response.setStatusCode("400");
                response.setStatusMsg("Invalid book data provided.");
                return response;
            }

            // Create and save book
            Book book = new Book();
            book.setAuthor(request.getAuthor());
            book.setGenre(request.getGenre());
            book.setAvailableCopies(request.getAvailableCopies());

            bookDao.save(book);

            logger.info("Book created successfully: {}", book);
            response.setStatus("SUCCESS");
            response.setStatusCode("200");
            response.setStatusMsg("Book created successfully.");
        } catch (Exception e) {
            logger.error("Error while creating book: {}", e.getMessage(), e);
            response.setStatus("FAILURE");
            response.setStatusCode("500");
            response.setStatusMsg("An unexpected error occurred: " + e.getMessage());
        }

        return response;
    }

    @Override
    public SortedBookResponseBean getByGenre() {
        logger.info("Fetching all books for sorting by author");

        List<Book> books = bookDao.findAll();

        if (books.isEmpty()) {
            logger.warn("No books found in the database.");
            throw new BookNotFoundException("No books available in the database.");
        }

        books.sort((b1, b2) -> b1.getAuthor().compareToIgnoreCase(b2.getAuthor()));
        logger.info("Books sorted by author name successfully.");

        SortedBookResponseBean response = new SortedBookResponseBean();
        response.setStatus("SUCCESS");
        response.setStatusCode("200");
        response.setStatusMsg("Books sorted by Author name.");
        response.setSortedBooks(books);

        return response;
    }

    @Override
    public BooksCountResponseBean getCountByAuthor() {
        logger.info("Fetching book count by author");

        List<Book> books = bookDao.findAll();

        if (books.isEmpty()) {
            logger.warn("No books found while counting by author");
            throw new BookNotFoundException("Unable to fetch books by Author");
        }

        Map<String, Integer> countMap = new HashMap<>();
        for (Book book : books) {
            String author = book.getAuthor();
            countMap.put(author, countMap.getOrDefault(author, 0) + 1);
        }

        logger.info("Book count by author fetched successfully: {}", countMap);

        BooksCountResponseBean response = new BooksCountResponseBean();
        response.setStatus("SUCCESS");
        response.setStatusCode("200");
        response.setStatusMsg("Book count fetched successfully.");
        response.setAuthorBookCount(countMap);

        return response;
    }
}
