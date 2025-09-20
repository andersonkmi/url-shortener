package org.codecraftlabs.shorturl.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public class URLShortnerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public URLShortnerRepository(@Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long getUrlId() throws DatabaseException {
        try {
            String statement = "SELECT NEXTVAL('url_id_sequence')";
            Long id = jdbcTemplate.queryForObject(statement, Long.class);
            if (id == null) {
                throw new DatabaseException("Failed to retrieve next value");
            }
            return id;
        } catch (DataAccessException exception) {
            throw new DatabaseException("Failed to get the next sequence value", exception);
        }
    }

    @Nonnull
    public Optional<ShortenedURL> findShortenedUrl(@Nonnull String url) {
        try {
            String query = "select url_id, url, short_url from url where url = ?";
            ShortenedURL item = jdbcTemplate.queryForObject(query, new ShortenedURLRowMapper(), url);
            return Optional.ofNullable(item);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Transactional(rollbackFor = DatabaseException.class)
    public void saveShortUrl(long id, @Nonnull String url, @Nonnull String shortUrl) throws DatabaseException {
        try {
            String statement = "insert into url(url_id, url, short_url) values (?, ?, ?)";
            jdbcTemplate.update(statement, id, url, shortUrl);
        } catch (DataAccessException exception) {
            throw new DatabaseException("Failed to insert new URL", exception);
        }
    }
}
