package com.example.demo.util;

import com.example.demo.domain.Category;
import com.example.demo.domain.Company;
import com.example.demo.domain.CompanySearchResult;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

/**
 * Created by platon on 27.01.18.
 */
@Log
public class ScraperUtil {

    private static final String HOST = "https://www.pegaxis.com";
    private static final String SERVICE_ROUTE = "/services";
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    public static List<Category> retrieveCategories(int timeout) {
        return getCategoryElementsByServicesDoc(getDocBy(HOST + SERVICE_ROUTE, timeout))
            .stream()
            .map(service -> Category
                .builder()
                .name(service.text())
                .uri(HOST + service.attr("href"))
                .build())
            .peek(c -> log.info(c.toString()))
            .collect(toList());
    }

    public static List<Company> retrieveCompaniesBy(Category category, int timeout) {
        return getCompanyNamesByServiceDoc(getDocBy(category.getUri(), timeout), category);
    }

    @SneakyThrows
    public static Document getDocBy(String uri, int timeout) {
        return Jsoup.connect(uri).timeout(timeout).get();
    }

    public static List<Element> getCategoryElementsByServicesDoc(Document serviceDoc) {
        return serviceDoc.getElementsByTag("a")
                .stream()
                .filter(el -> el.hasClass("service_list_title"))
                .collect(toList());
    }

    @SneakyThrows(IOException.class)
    public static List<CompanySearchResult> retrieveCompanySearchResults(Company company, int resultCount, int timeout) {
        List<CompanySearchResult> resultList = null;
        String searchUri = GOOGLE_SEARCH_URL + "?q=" + company.getName() + "&num=" + resultCount;
        Random random = new Random();
        int ranTimeout = random.nextInt((5000 - 3000) + 1) + 3000;
//        System.setProperty("http.proxyHost", "42.104.84.107"); // set proxy server
//        System.setProperty("http.proxyPort", "8080");  //
        Document doc = null;
        try {
            doc = Jsoup.connect(searchUri).userAgent("Mozilla/5.0").timeout(ranTimeout).get();
            Elements resultElements = doc.select("h3.r > a");
            resultList = new ArrayList<>();

            for (Element result : resultElements) {
                String linkHref = result.attr("href");
                String linkText = result.text();
                CompanySearchResult searchResult = CompanySearchResult.builder()
                        .company(company)
                        .uri(linkHref.substring(6, linkHref.indexOf("&")))
                        .title(linkText)
                        .build();
                resultList.add(searchResult);
                log.info(searchResult.toString());
            }
        } catch (org.jsoup.HttpStatusException e) {
            log.severe(e.getMessage());
        }
        return resultList;
    }

    public static List<Company> getCompanyNamesByServiceDoc(Document serviceDoc, Category category) {
        return serviceDoc.getElementsByClass("company-name")
                .parallelStream()
                .map(serviceEl -> Company.builder()
                        .name(serviceEl.getElementsByTag("a").get(0).text())
                        .category(category)
                        .build())
                .peek(c -> log.info(c.toString()))
                .collect(toList());
    }

}
