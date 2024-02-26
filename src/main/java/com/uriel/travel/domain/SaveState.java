package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SaveState implements CodedEnum<String> {

    TEMPORARY("임시저장"),
    SAVED("저장");

    private final String viewName;

    @JsonCreator
    public static SaveState from(String sub) {
        for (SaveState isSaved : SaveState.values()) {
            if (isSaved.getViewName().equals(sub)) {
                return isSaved;
            }
        }
        return null;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<SaveState, String> {
        public Converter() {
            super(SaveState.class);
        }
    }
}
