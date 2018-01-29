package com.example.demo.web;

import com.example.demo.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by platon on 27.01.18.
 */
@Controller
public class ScraperController {

    private final CompanyService companyService;

    public ScraperController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/data")
    public String showCompaniesData(ModelMap map) {
        map.addAttribute("companies", companyService.getCompanies());
        return "companies";
    }

}
