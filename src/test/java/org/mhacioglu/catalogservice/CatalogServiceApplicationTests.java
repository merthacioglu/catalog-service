package org.mhacioglu.catalogservice;

import org.junit.jupiter.api.Test;
import org.mhacioglu.catalogservice.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Test
    void contextLoads() {
    }

    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = Book.build("1231231231", Instant.now(), Instant.now(),
                "Title", "Author", 9.90);
        client.post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                   assertThat(actualBook).isNotNull();
                   assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

}
