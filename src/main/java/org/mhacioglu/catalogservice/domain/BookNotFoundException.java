package org.mhacioglu.catalogservice.domain;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String isbn){
        super("A book with ISBN " + isbn + " does not exist.");
    }
}
