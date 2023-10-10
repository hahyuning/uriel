package com.uriel.travel.domain;

import com.uriel.travel.domain.enumeration.Authority;
import com.uriel.travel.domain.enumeration.Gender;
import com.uriel.travel.jwt.entity.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate createdDate;
    LocalDate modifiedDate;
    String userName;
    LocalDate birth;
    String email; //=회원 아이디
    String password;
    String phoneNumber;
    int headCount; //가족 인원수
    String childName;
    Gender gender;
    @OneToOne
    RefreshToken refreshToken;
    Authority authority;

//    @OneToMany(mappedBy = "users")
//    List <Reservation> reservationList=new ArrayList<>();
//    @OneToOne(mappedBy = "users")
//    JwtRefreshToken jwtRefreshToken;
    public void encodePassword(PasswordEncoder passwordEncoder){
        password=passwordEncoder.encode(password);
    }
    public void setEncodePassword(String password, PasswordEncoder passwordEncoder) {
        if (StringUtils.hasText(password)) {
            this.password = password;
            encodePassword(passwordEncoder);
        }
    }
    public void setRefreshToken(RefreshToken refreshToken){
        this.refreshToken=refreshToken;
    }
    public void setAuthority(Authority authority){
        this.authority=authority;
    }

}
