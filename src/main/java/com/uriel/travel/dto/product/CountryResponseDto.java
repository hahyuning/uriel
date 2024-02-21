package com.uriel.travel.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.uriel.travel.domain.Country;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryResponseDto {

    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CountryInfo {
        Long countryId;
        String countryName;

        public static CountryResponseDto.CountryInfo of (Country country) {
            return CountryInfo.builder()
                    .countryId(country.getId())
                    .countryName(country.getCountryName()).build();
        }
    }
}
