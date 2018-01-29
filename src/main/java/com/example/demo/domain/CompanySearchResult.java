package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by platon on 27.01.18.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanySearchResult {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String uri;
    private String title;

    @ManyToOne
    private Company company;

}
