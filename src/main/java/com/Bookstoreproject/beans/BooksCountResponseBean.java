package com.Bookstoreproject.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BooksCountResponseBean {
    public String status;
    public String statusCode;
    public String statusMsg;
    public Map<String, Integer> authorBookCount;
}
