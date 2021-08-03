package com.desafio.tds.urlshortener.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "urlStats")
@Data
@NoArgsConstructor
public class UrlStats {

    @MongoId
    private ObjectId id;

    private String shortUrl;

    private Long totalAccess;

    private String lastAccess;
}
