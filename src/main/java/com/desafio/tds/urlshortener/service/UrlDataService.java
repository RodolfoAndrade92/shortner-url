package com.desafio.tds.urlshortener.service;

import com.desafio.tds.urlshortener.domain.UrlData;
import com.desafio.tds.urlshortener.domain.UrlStats;
import com.desafio.tds.urlshortener.domain.dto.UrlDTO;
import com.desafio.tds.urlshortener.repository.UrlDataRepository;
import com.desafio.tds.urlshortener.repository.UrlStatsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UrlDataService {

    @Autowired
    private UrlDataRepository urlDataRepository;

    @Autowired
    private UrlStatsRepository urlStatsRepository;

    private static final String BASE_URL = "http://shrt.url/";

    private static final String LETTERS = "1234567890abcdefghijklmnopqrstuvwxyz";

    @Transactional
    public UrlData create(final UrlDTO urlDTO) {
        UrlData urlData = new UrlData();

        urlData.setFullUrl(urlDTO.getParam());

        urlData.setShortUrl(this.createShortUrl());

        return urlDataRepository.save(urlData);
    }

    public List<UrlData> getUrlData(final UrlDTO urlDTO) {
        if (urlDTO.getParam() == null || urlDTO.getParam().isEmpty() || urlDTO.getParam().isBlank()) {
            return urlDataRepository.findAll();
        } else {
            List<UrlData> urlDataList = new ArrayList<>();
            Optional<UrlData> urlDataOptional = urlDataRepository.findById(urlDTO.getParam());
            urlDataOptional.ifPresent(urlDataList::add);
            UrlData byShortUrl = urlDataRepository.findByShortUrl(urlDTO.getParam());
            if (byShortUrl != null) {
                urlDataList.add(byShortUrl);
            }
            return urlDataList;
        }
    }

    @Transactional
    public UrlData find(final UrlDTO urlDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        UrlData urlData = urlDataRepository.findByShortUrl(urlDTO.getParam());
        final boolean urlStatsExist = urlStatsRepository.existsById(urlData.getId().toHexString());
        if (!urlStatsExist) {
            UrlStats urlStats = new UrlStats();
            urlStats.setId(urlData.getId());
            urlStats.setShortUrl(urlData.getShortUrl());
            urlStats.setTotalAccess(1L);
            urlStats.setLastAccess(sdf.format(date));

            urlStatsRepository.save(urlStats);
        } else {
            Optional<UrlStats> urlStatsOptional = urlStatsRepository.findById(urlData.getId().toHexString());
            if (urlStatsOptional.isPresent()) {
                UrlStats urlStats = urlStatsOptional.get();
                Long totalAccess = urlStats.getTotalAccess();

                urlStats.setTotalAccess(++totalAccess);
                urlStats.setLastAccess(sdf.format(date));

                urlStatsRepository.save(urlStats);
            }
        }

        return urlData;
    }

    public UrlStats findUrlStats(final UrlDTO urlDTO) {
        return urlStatsRepository.findByShortUrl(urlDTO.getParam());
    }

    private String createShortUrl() {

        String shortUrlID = RandomStringUtils.random(10, LETTERS);

        String shortUrl = BASE_URL + shortUrlID;

        boolean existsShortUrl = urlDataRepository.existsByShortUrl(shortUrl);

        if (existsShortUrl) {
            return this.createShortUrl();
        } else {
            return shortUrl;
        }
    }
}
