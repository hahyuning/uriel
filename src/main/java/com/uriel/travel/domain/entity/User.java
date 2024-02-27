package com.uriel.travel.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriel.travel.domain.Gender;
import com.uriel.travel.domain.Role;
import com.uriel.travel.domain.dto.user.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String krName;
    String enFirstName;
    String enLastName;
    LocalDate birth;

    Gender gender;

    String email; //=회원 아이디
    String password;

    String phoneNumber;

    @Builder.Default
    int headCount = 0; //가족 인원수
    String childName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Role role = Role.ROLE_USER;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "reserveUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Order> orderList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return simpleGrantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void update(UserRequestDto.Update requestDto) {
        this.krName = requestDto.getUserName();
        this.enFirstName = requestDto.getEnFirstName();
        this.enLastName = requestDto.getEnLastName();
        this.gender = Gender.from(requestDto.getGender());
        this.birth = requestDto.getBirth();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.headCount = requestDto.getHeadCount();
        this.childName = requestDto.getChildName();
    }
}
