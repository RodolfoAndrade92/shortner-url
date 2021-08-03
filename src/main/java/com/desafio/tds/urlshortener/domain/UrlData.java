package com.desafio.tds.urlshortener.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "urlData")
@Data
@NoArgsConstructor
public class UrlData {

    @MongoId
    private ObjectId id;

    private String fullUrl;

    private String shortUrl;
}
