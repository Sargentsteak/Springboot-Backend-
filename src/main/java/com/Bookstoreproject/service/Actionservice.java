package com.Bookstoreproject.service;

import com.Bookstoreproject.beans.BooksCountResponseBean;
import com.Bookstoreproject.beans.CreateBookRequest;
import com.Bookstoreproject.beans.ResponseBeanBook;
import com.Bookstoreproject.beans.SortedBookResponseBean;

public interface Actionservice {

    ResponseBeanBook createBook(CreateBookRequest request);
    SortedBookResponseBean getByGenre();
    BooksCountResponseBean getCountByAuthor();

}
