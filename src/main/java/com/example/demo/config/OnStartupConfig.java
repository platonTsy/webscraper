package com.example.demo.config;

import com.example.demo.domain.Category;
import com.example.demo.domain.Company;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CompanySearchResultRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.util.ScraperUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by platon on 27.01.18.
 */
@Component
public class OnStartupConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final CompanySearchResultRepository companySearchResultRepository;

    private int timeout = 60 * 1000;
    private int resultCount = 2;

    public OnStartupConfig(CategoryRepository categoryRepository, CompanyRepository companyRepository, CompanySearchResultRepository companySearchResultRepository) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
        this.companySearchResultRepository = companySearchResultRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        List<Category> categories = saveCategories();
        saveCompanies(categories);
    }

    @Transactional
    private List<Category> saveCategories() {
        List<Category> categories = ScraperUtil.retrieveCategories(timeout);
        return categoryRepository.save(categories);
    }

    @Transactional
    private List<Company> saveCompanies(List<Category> categories) {
        return categories.parallelStream()
                .map(c -> ScraperUtil.retrieveCompaniesBy(c, timeout))
                .flatMap(Collection::stream)
                .peek(this::saveCompany)
                .peek(this::saveCompanySearchResults)
                .collect(toList());
    }

    private synchronized Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    private synchronized void saveCompanySearchResults(Company company) {
        companySearchResultRepository.save(ScraperUtil.retrieveCompanySearchResults(company, resultCount, timeout));
    }
}
