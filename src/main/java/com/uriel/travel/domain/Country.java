package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country implements CodedEnum<String> {

    EEP("동유럽"),
    EW1("프랑스위스"),
    EW2("스페인"),
    EW3("이탈리아"),
    PAE("호주"),
    PNN("뉴질랜드"),
    ADP("대만"),
    JHP("일본"),
    AAP("동남아"),
    KID("키자니아");

    private final String viewName;

    @JsonCreator
    public static Country from(String sub) {
        for (Country country : Country.values()) {
            if (country.getViewName().equals(sub)) {
                return country;
            }
        }
        return null;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<Country, String> {
        public Converter() {
            super(Country.class);
        }
    }
}
