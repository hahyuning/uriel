package com.uriel.travel.domain.dto.community;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageDto {
    String originalImageName;
    String uploadImageName;
    String imagePath;
    String imageUrl;
}
