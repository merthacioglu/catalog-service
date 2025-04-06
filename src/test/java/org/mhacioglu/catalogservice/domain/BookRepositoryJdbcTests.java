package org.mhacioglu.catalogservice.domain;

import org.junit.jupiter.api.Test;
import org.mhacioglu.catalogservice.persistence.DataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllBooks() {
        var book1 = new Book(null, Instant.now(), Instant.now(),
                "1231231234", "Test title 1", "Test author 1",
                22.90, "Test publisher", 0);
        var book2 = new Book(null, Instant.now(), Instant.now(),
                "0987654321", "Test title 2", "Test author 2",
                16.90, "Test publisher", 0);
        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<Book> actualBooks = bookRepository.findAll();

        assertThat(StreamSupport.stream(actualBooks.spliterator(), true)
                .filter(book -> book.isbn().equals(book1.isbn()) || book.isbn().equals(book2.isbn()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findBookByIsbnWhenExisting() {
        String isbn = "1231231234";
        var book = new Book(null, Instant.now(), Instant.now(),
                isbn, "Test title 1", "Test author 1",
                22.90, "Test publisher", 0);
        jdbcAggregateTemplate.insert(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        Optional<Book> actualBook = bookRepository.findByIsbn("1234561238");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void existsByIsbnWhenExisting() {
        String isbn = "1234561239";
        var book = new Book(null, Instant.now(), Instant.now(),
                isbn, "Test title 1", "Test author 1",
                22.90, "Test publisher", 0);
        jdbcAggregateTemplate.insert(book);

        boolean existing = bookRepository.existsByIsbn(isbn);

        assertThat(existing).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        boolean existing = bookRepository.existsByIsbn("1234561240");
        assertThat(existing).isFalse();
    }

    @Test
    void deleteByIsbn() {
        var isbn = "1234561241";
        var book = new Book(null, Instant.now(), Instant.now(),
                isbn, "Test title 1", "Test author 1",
                22.90, "Test publisher", 0);
        var persistedBook = jdbcAggregateTemplate.insert(book);

        bookRepository.deleteByIsbn(persistedBook.isbn());

        assertThat(jdbcAggregateTemplate.findById(persistedBook.id(), Book.class)).isNull();
    }
}
