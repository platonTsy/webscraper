package com.example.demo.repository;

import com.example.demo.domain.Company;
import com.example.demo.domain.CompanySearchResult;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by platon on 27.01.18.
 */
public interface CompanySearchResultRepository extends JpaRepository<CompanySearchResult, Long> {
}
