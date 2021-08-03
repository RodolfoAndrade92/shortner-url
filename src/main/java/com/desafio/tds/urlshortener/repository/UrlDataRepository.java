package com.desafio.tds.urlshortener.repository;

import com.desafio.tds.urlshortener.domain.UrlData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlDataRepository extends MongoRepository<UrlData, String> {
    UrlData findByShortUrl(final String url);

    boolean existsByShortUrl(final String url);
}
