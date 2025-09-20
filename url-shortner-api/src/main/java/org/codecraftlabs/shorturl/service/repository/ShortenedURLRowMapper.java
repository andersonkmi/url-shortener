package org.codecraftlabs.shorturl.service.repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class ShortenedURLRowMapper implements RowMapper<ShortenedURL> {
    @Override
    public ShortenedURL mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("url_id");
        String url = rs.getString("url");
        String shortUrl = rs.getString("short_url");
        return new ShortenedURL(id, url, shortUrl);
    }
}
