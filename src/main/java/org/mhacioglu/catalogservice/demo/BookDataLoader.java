package org.mhacioglu.catalogservice.demo;

import org.mhacioglu.catalogservice.domain.Book;
import org.mhacioglu.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Profile("test-data")
public class BookDataLoader {
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();
        var book1 = Book.build("1234567891", Instant.now(), null, "Northern Lights", "Lyra Silvertongue", 9.90, null);
        var book2 = Book.build("1234567892", Instant.now(), null,"Polar Journey", "Iorek Polarson", 12.90, null);

        bookRepository.saveAll(List.of(book1, book2));

    }
}
