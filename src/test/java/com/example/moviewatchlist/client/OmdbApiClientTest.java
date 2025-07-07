package com.example.moviewatchlist.client;

import com.example.moviewatchlist.config.OmdbApiConfig;
import com.example.moviewatchlist.dto.omdb.OmdbMovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@DisplayName("OmdbApiClient")
class OmdbApiClientTest {

  private MockRestServiceServer server;
  private OmdbApiClient client;

  @BeforeEach
  void init() {
    OmdbApiConfig cfg = new OmdbApiConfig();
    cfg.setUrl("https://omdbapi.com/");
    cfg.setKey("k");

    RestTemplate rest = new RestTemplate();
    RestTemplateBuilder b = Mockito.mock(RestTemplateBuilder.class);
    Mockito.when(b.build()).thenReturn(rest);

    server = MockRestServiceServer.bindTo(rest).build();
    client = new OmdbApiClient(b, cfg);
  }

  @Test
  @DisplayName("getMovieData - returns DTO for given title")
  void getMovieData_returnsDto() {
    // Arrange
    String json = """
        { "Title":"Inception","Year":"2010","Director":"Christopher Nolan",
          "Genre":"Action","imdbID":"tt1375666","Poster":"p.jpg","Response":"True" }""";
    server.expect(requestTo(allOf(
        containsString("t=Inception"),
        containsString("apikey=k"))))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

    // Act
    OmdbMovieDto dto = client.getMovieData("Inception");

    // Assert
    assertThat(dto).isNotNull()
        .extracting(OmdbMovieDto::getTitle, OmdbMovieDto::getImdbId)
        .containsExactly("Inception", "tt1375666");
    server.verify();
  }
}
