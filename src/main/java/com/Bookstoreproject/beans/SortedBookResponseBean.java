package com.Bookstoreproject.beans;

import com.Bookstoreproject.entity.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SortedBookResponseBean extends ResponseBeanBook {
    private List<Book> sortedBooks;
}
