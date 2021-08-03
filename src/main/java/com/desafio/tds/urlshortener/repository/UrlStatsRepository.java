package com.desafio.tds.urlshortener.repository;

import com.desafio.tds.urlshortener.domain.UrlStats;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlStatsRepository extends MongoRepository<UrlStats, String> {
    UrlStats findByShortUrl(final String url);
}
