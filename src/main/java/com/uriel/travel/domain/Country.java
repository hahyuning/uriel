package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Country {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    Long id;

    @Column(unique = true)
    String countryName;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Package> packageList = new ArrayList<>();
}
