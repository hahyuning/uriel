package com.uriel.travel.dto.product;

import com.uriel.travel.domain.Country;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

public class CountryRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {

        Long countryId;
        String countryName;

        public Country toEntity() {
            return Country.builder()
                    .countryName(countryName).build();
        }
    }
}
