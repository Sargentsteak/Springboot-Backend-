package com.Bookstoreproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
   private Integer Id;
    @Column(name="author")
    private String author;
    @Column(name ="genre")
    private String genre;
    @Column(name = "available_copies")
    private int availableCopies;


}
