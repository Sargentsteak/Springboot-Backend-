package com.Bookstoreproject.controller;

import com.Bookstoreproject.beans.BooksCountResponseBean;
import com.Bookstoreproject.beans.CreateBookRequest;
import com.Bookstoreproject.beans.ResponseBeanBook;
import com.Bookstoreproject.beans.SortedBookResponseBean;
import com.Bookstoreproject.exceptions.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Bookstoreproject.service.Actionservice;

@RestController
@Slf4j
@RequestMapping("/book")
public class ActionController {

    private static final Logger logger = LoggerFactory.getLogger("ActionController");

    @Autowired
    Actionservice actionservice;

    @PostMapping("/createBook")
    public ResponseEntity<ResponseBeanBook> createBook(@RequestBody CreateBookRequest request) {
        logger.info("Entering createBook API");
        try {
            ResponseBeanBook response = actionservice.createBook(request);
            logger.info("Book created successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in createBook API", e);
            throw new RuntimeException("Failed to create book", e);
        }
    }

    @GetMapping("/getByGenre")
    public ResponseEntity<SortedBookResponseBean> getByGenre(){
        logger.info("Entering getByGenre API");
        try{
            SortedBookResponseBean responseBean = actionservice.getByGenre();
            logger.info("Books sorted successfully by Genre");
            return ResponseEntity.ok(responseBean);
        }catch (Exception e){
            logger.error("Error while fetching books by genre" ,e);
            throw new RuntimeException("Failed to fetch books by Genre",e);
        }
    }

    @GetMapping("/getCountByAuthor")
    public ResponseEntity<BooksCountResponseBean> countByAuthor(){
        logger.info("Entering the getCountByAuthor API");
        try{
            BooksCountResponseBean responseBean = actionservice.getCountByAuthor();
            logger.info("Books by Author fetched sucessfully");
            return ResponseEntity.ok(responseBean);
        } catch (Exception e) {
            logger.error("Error while fetching Books by author");
            throw new RuntimeException("Failed to fetch books by Author");

        }
    }
}
