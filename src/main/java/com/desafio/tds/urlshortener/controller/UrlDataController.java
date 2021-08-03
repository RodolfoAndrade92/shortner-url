package com.desafio.tds.urlshortener.controller;

import com.desafio.tds.urlshortener.domain.UrlData;
import com.desafio.tds.urlshortener.domain.UrlStats;
import com.desafio.tds.urlshortener.domain.dto.UrlDTO;
import com.desafio.tds.urlshortener.service.UrlDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/url-data")
public class UrlDataController {

    @Autowired
    private UrlDataService urlDataService;

    @PostMapping(value = "/create-url-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<UrlData> createUrlData(@RequestBody UrlDTO urlDTO) {
        UrlData urlData = urlDataService.create(urlDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(urlData.getId()).toUri();

        return ResponseEntity.created(uri).body(urlData);
    }

    @PostMapping(value = "/get-url-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<UrlData>> getUrlData(@RequestBody UrlDTO urlDTO) {
        List<UrlData> urlDataList = urlDataService.getUrlData(urlDTO);
        return ResponseEntity.ok((ArrayList<UrlData>) urlDataList);
    }

    @PostMapping(value = "/execute-redirect", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> executeRedirect(@RequestBody UrlDTO urlDTO) throws URISyntaxException {
        UrlData urlData = urlDataService.find(urlDTO);
        URI uri = new URI(urlData.getFullUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(value = "/get-url-stats", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlStats> getUrlStats(@RequestBody UrlDTO urlDTO) {
        return ResponseEntity.ok(urlDataService.findUrlStats(urlDTO));
    }
}
