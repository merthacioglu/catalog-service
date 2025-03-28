package org.mhacioglu.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

public record Book (

        @Id
        Long id,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @NotBlank(message = "The book ISBN must be defined.")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ISBN format must follow the standards ISBN-10 or ISBN-13.")
        String isbn,
        @NotBlank(message = "The book title must be defined.")
        String title,
        @NotBlank(message = "The book author must be defined.")
        String author,
        @NotNull(message = "The book price must be defined.")
                @Positive(message = "The book price must be greater than 0.")
        Double price,

        String publisher,

        @Version
        int version

){
        public static Book build(String isbn, Instant createdDate, Instant lastModifiedDate,
                                 String title, String author, Double price, String publisher) {
                return new Book(null, createdDate, lastModifiedDate, isbn, title, author, price,  publisher, 0);
        }
}
