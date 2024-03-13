package com.uriel.travel.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uriel.travel.domain.SocialType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String name;
        private String email;
        private String gender;
        private String birthday;
        private String birthyear;
        private String mobile;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getName() {
        return response.name;
    }

    @Override
    public String getGender() { return response.gender; }

    @Override
    public String getBirthday() {
        return response.birthday;
    }

    @Override
    public String getBirthyear() {
        return response.birthyear;
    }

    @Override
    public String getPhoneNumber() {
        return response.mobile;
    }

    @Override
    public LocalDate getBirth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (response.birthday != null && response.birthyear != null) {
            return LocalDate.parse(response.birthyear + "-" + response.birthday, formatter);
        }
        return null;
    }

    @Override
    public SocialType getOAuthProvider() {
        return null;
    }
}
