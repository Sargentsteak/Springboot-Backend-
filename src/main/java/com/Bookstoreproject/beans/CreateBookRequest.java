package com.Bookstoreproject.beans;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequest {
    private String author;
    private String genre;
    private int availableCopies;
}
