package com.example.demo.repository;

import com.example.demo.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by platon on 27.01.18.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
