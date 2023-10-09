package com.uriel.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageDto {
    private String originalImageName;
    private String uploadImageName;
    private String imagePath;
    private String imageUrl;
}
