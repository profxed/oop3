## Movie Watchlist API

A minimal Spring Boot backend to search, save, and manage movies from OMDb and TMDB.

---

### 🛠️ Prerequisites

* Java 21
* Maven 3.6+
* Docker & Docker Compose (for MySQL)
* OMDb API key
* TMDB API key

---

### 🚀 Quick Start

1. **Clone & checkout**

   ```bash
   git clone <repo-url>
   cd movie-watchlist
   git checkout feature/movie-crud
   ```

2. **Environment**
   Copy `.env.sample` → `.env` and fill in:

   ```dotenv
   DB_URL=jdbc:jdbc:h2:mem:testdb
   DB_USERNAME=user
   DB_PASSWORD=password
   OMDB_API_KEY=your_omdb_key
   TMDB_API_KEY=your_tmdb_key
   ```

3. **Build & run**

   ```bash
   mvn clean package
   java -jar target/movie-watchlist-0.0.1-SNAPSHOT.jar
   ```

---

### 🔌 Endpoints

#### 1. Search (no DB write)

```
GET /movies/search?title={title}
```

* **Response**:

  ```json
  [
    {
      "source":"omdb",
      "imdbId":"tt1375666",
      "tmdbId":27205,
      "title":"Inception",
      "year":"2010",
      "director":"Christopher Nolan",
      "genre":"Action, Adventure, Sci-Fi",
      "poster":"<omdb-poster-url>",
      "releaseDate":"2010-07-15",
      "voteAverage":7.8,
      "voteCount":25000
    },
    {
      "source":"tmdb",
      "imdbId":"tt1375666",
      "tmdbId":27205,
      "title":"Inception",
      "poster":"<tmdb-poster-url>",
      "releaseDate":"2010-07-15",
      "voteAverage":7.05,
      "voteCount":10
    }
  ]
  ```

#### 2. Add & fetch (persist to DB)

```
POST /movies?title={title}
```

* **Response**: same as **Search** output, plus persist.

#### 3. List (paginated)

```
GET /movies?page={0‑n}&size={10‑100}
```

* **Response**:

  ```json
  {
    "content":[ [<MovieDataDto>,…], … ],
    "pageable":{…},
    "totalPages":5,
    …
  }
  ```

#### 4. Update watched

```
PATCH /movies/{id}/watched?watched={true|false}
```

#### 5. Update rating

```
PATCH /movies/{id}/rating?rating={1‑5}
```

#### 6. Delete

```
DELETE /movies/{id}
```

---

### 📁 Project Structure

```
src/
 ├─ main/
 │   ├─ java/com/example/moviewatchlist/
 │   │   ├─ client/       – OMDb & TMDB REST clients  
 │   │   ├─ config/       – API key & URL config  
 │   │   ├─ controller/   – HTTP endpoints  
 │   │   ├─ dto/          – MovieDataDto, OMDb & TMDB DTOs  
 │   │   ├─ model/        – JPA Movie entity  
 │   │   ├─ repository/   – JpaRepository<Movie, Long>  
 │   │   └─ service/      – Business logic  
 │   └─ resources/
 │       └─ application.properties
 └─ test/               – Integration tests
```

for a complete list of the API's endpoints you can always use your browser:
```
GET /swagger-ui.html
```

for a db panel-control and console:
```
GET /h2-console
```

---

Enjoy your Movie Watchlist API!
