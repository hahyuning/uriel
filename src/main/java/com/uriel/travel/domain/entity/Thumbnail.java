package com.uriel.travel.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumbnail_id")
    Long id;

    String originalImageName;

    @Column(unique = true)
    String uploadImageName;

    String imagePath;

    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    Package aPackage;

    public Thumbnail(String originalImageName, String uploadImageName, String imagePath, String imageUrl) {
        this.originalImageName = originalImageName;
        this.uploadImageName = uploadImageName;
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }

    public void setPackage(Package aPackage) {
        this.aPackage = aPackage;
        aPackage.getThumbnailList().add(this);
    }

    public void idInit() {
        this.id = null;
    }
}
