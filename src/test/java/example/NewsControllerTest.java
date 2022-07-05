package example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import jakarta.inject.Inject;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class NewsControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/news")
    HttpClient client;

    @Timeout(4) // <1>
    @Test
    void fetchingOctoberHeadlinesUsesCache() {
        HttpRequest request = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name()).build());
        News news = client.toBlocking().retrieve(request, News.class);
        String expected = "Oct 1";
        assertEquals(Arrays.asList(expected), news.getHeadlines());

        News news2 = client.toBlocking().retrieve(request, News.class);
        assertEquals(Arrays.asList(expected), news2.getHeadlines());
    }
}
