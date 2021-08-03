package com.desafio.tds.urlshortener.integration;

import com.desafio.tds.urlshortener.domain.UrlData;
import com.desafio.tds.urlshortener.domain.UrlStats;
import com.desafio.tds.urlshortener.domain.dto.UrlDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlDataControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private static final String GOOGLE_ADDR = "http://www.google.com";

    @AfterEach
    void clearDB(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().drop();
    }

    @Test
    void createUrlDataSuccess() {
        UrlDTO urlDTO = this.getUrlDTO(GOOGLE_ADDR);

        ResponseEntity<UrlData> response = this.getUrlDataResponse(urlDTO);

        UrlData urlDataResponse = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(urlDataResponse);
        assertNotNull(urlDataResponse.getId());
        assertNotNull(urlDataResponse.getFullUrl());
        assertNotNull(urlDataResponse.getShortUrl());

    }

    @Test
    void getUrlDataSuccess() {
        UrlDTO urlDTO1 = this.getUrlDTO(GOOGLE_ADDR);
        UrlDTO urlDTO2 = this.getUrlDTO("http://www.facebook.com");
        UrlDTO urlDTO3 = this.getUrlDTO("http://www.instagram.com");

        UrlData urlData = this.getUrlDataResponse(urlDTO1).getBody();
        this.getUrlDataResponse(urlDTO2);
        this.getUrlDataResponse(urlDTO3);

        UrlDTO emptyParamUrlDTO = this.getUrlDTO("");
        UrlDTO paramShortUrlDTO = this.getUrlDTO(Objects.requireNonNull(urlData).getShortUrl());

        ResponseEntity<ArrayList<UrlData>> emptyParamUrlDataListResponse = this.getUrlDataListResponse(emptyParamUrlDTO);
        ResponseEntity<ArrayList<UrlData>> paramShortUrlDataListResponse = this.getUrlDataListResponse(paramShortUrlDTO);

        assertEquals(HttpStatus.OK, emptyParamUrlDataListResponse.getStatusCode());
        assertEquals(HttpStatus.OK, paramShortUrlDataListResponse.getStatusCode());
        assertEquals(3, Objects.requireNonNull(emptyParamUrlDataListResponse.getBody()).size());
        assertEquals(1, Objects.requireNonNull(paramShortUrlDataListResponse.getBody()).size());

    }

    @Test
    void executeRedirectSuccess() {
        UrlDTO createUrlDTO = this.getUrlDTO(GOOGLE_ADDR);

        UrlData createdUrlData = this.getUrlDataResponse(createUrlDTO).getBody();

        UrlDTO executeRedirectUrlDTO = this.getUrlDTO(Objects.requireNonNull(createdUrlData).getShortUrl());

        ResponseEntity<?> executeRedirectResponse = this.executeRedirectResponse(executeRedirectUrlDTO);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, executeRedirectResponse.getStatusCode());
        assertEquals(GOOGLE_ADDR, Objects.requireNonNull(executeRedirectResponse.getHeaders().getLocation()).toString());

    }

    @Test
    void getUrlStatsSuccess() {
        UrlDTO createUrlDTO = this.getUrlDTO(GOOGLE_ADDR);

        UrlData createdUrlData = this.getUrlDataResponse(createUrlDTO).getBody();

        UrlDTO executeRedirectUrlDTO = this.getUrlDTO(Objects.requireNonNull(createdUrlData).getShortUrl());

        this.executeRedirectResponse(executeRedirectUrlDTO);
        this.executeRedirectResponse(executeRedirectUrlDTO);
        this.executeRedirectResponse(executeRedirectUrlDTO);

        ResponseEntity<UrlStats> urlStatsResponse = this.getUrlStatsResponse(executeRedirectUrlDTO);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();

        assertEquals(HttpStatus.OK, urlStatsResponse.getStatusCode());
        assertEquals(3L, Objects.requireNonNull(urlStatsResponse.getBody()).getTotalAccess());
        assertEquals(createdUrlData.getShortUrl(), urlStatsResponse.getBody().getShortUrl());
        assertEquals(sdf.format(date), urlStatsResponse.getBody().getLastAccess());

    }

    private UrlDTO getUrlDTO(final String param) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setParam(param);
        return urlDTO;
    }

    private ResponseEntity<UrlData> getUrlDataResponse(final UrlDTO urlDTO) {
        HttpEntity<UrlDTO> request = new HttpEntity<>(urlDTO);
        ParameterizedTypeReference<UrlData> reference = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(getURL("create-url-data"), HttpMethod.POST, request, reference);
    }

    private ResponseEntity<ArrayList<UrlData>> getUrlDataListResponse(final UrlDTO urlDTO) {
        HttpEntity<UrlDTO> request = new HttpEntity<>(urlDTO);
        ParameterizedTypeReference<ArrayList<UrlData>> reference = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(getURL("get-url-data"), HttpMethod.POST, request, reference);

    }

    private ResponseEntity<?> executeRedirectResponse(final UrlDTO urlDTO) {
        HttpEntity<UrlDTO> request = new HttpEntity<>(urlDTO);
        ParameterizedTypeReference<?> reference = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(getURL("execute-redirect"), HttpMethod.POST, request, reference);
    }

    private ResponseEntity<UrlStats> getUrlStatsResponse(final UrlDTO urlDTO) {
        HttpEntity<UrlDTO> request = new HttpEntity<>(urlDTO);
        ParameterizedTypeReference<UrlStats> reference = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(getURL("get-url-stats"), HttpMethod.POST, request, reference);

    }

    private String getURL(final String resource) {
        return "http://localhost:" + port + "/shortnerurl/api/url-data/" + resource;
    }
}
