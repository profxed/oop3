package com.example.moviewatchlist.client;

import com.example.moviewatchlist.config.TmdbApiConfig;
import com.example.moviewatchlist.dto.tmdb.TmdbMovieSearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@DisplayName("TmdbApiClient")
class TmdbApiClientTest {

  private MockRestServiceServer server;
  private TmdbApiClient client;
  private TmdbApiConfig cfg;

  @BeforeEach
  void init() {
    cfg = new TmdbApiConfig();
    cfg.setBaseUrl("https://api.themoviedb.org/3/");
    cfg.setKey("k");
    cfg.setImageBaseUrl("i");

    RestTemplate restTemplate = new RestTemplate();
    RestTemplateBuilder builder = Mockito.mock(RestTemplateBuilder.class);
    Mockito.when(builder.build()).thenReturn(restTemplate);

    server = MockRestServiceServer.bindTo(restTemplate).build();
    client = new TmdbApiClient(builder, cfg);
  }

  @Test
  @DisplayName("searchMovie – builds correct URI and deserialises payload")
  void searchMovie_buildsCorrectUri() {
    // Arrange
    String json = """
        { "page":1,
          "results":[{ "id":27205, "title":"Inception", "poster_path":"/p.jpg", "release_date":"2010-07-16"}],
          "total_pages":1, "total_results":1 }""";
    server.expect(requestTo(allOf(
        containsString(cfg.getBaseUrl() + "search/movie"),
        containsString("api_key=k"),
        containsString("query=Inception"))))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

    // Act
    TmdbMovieSearchResponseDto dto = client.searchMovie("Inception");

    // Assert
    assertThat(dto).isNotNull();
    assertThat(dto.getResults()).singleElement()
        .extracting(r -> r.getTitle()).isEqualTo("Inception");
    server.verify();
  }
}
