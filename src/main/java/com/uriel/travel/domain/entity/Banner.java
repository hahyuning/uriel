package com.uriel.travel.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    Long id;

    String originalImageName;

    @Column(unique = true)
    String uploadImageName;

    String imagePath;

    String imageUrl;

    public Banner(String originalImageName, String uploadImageName, String imagePath, String imageUrl) {
        this.originalImageName = originalImageName;
        this.uploadImageName = uploadImageName;
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }
}
