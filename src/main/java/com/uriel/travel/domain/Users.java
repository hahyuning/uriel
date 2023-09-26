package com.uriel.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import java.util.*;
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    Date created_date;
    @Column
    Date modified_date;
    @Column
    String userName;
    @Column
    Date birth;
    @Column
    String email; //=회원 아이디
    @Column
    String password;
    @Column
    String phoneNumber;
    @Column
    int headCount; //가족 인원수
    @Column
    String childName;
    @Column
    String sex;
//    @OneToMany(mappedBy = "users")
//    List <Reservation> reservationList=new ArrayList<>();
//    @OneToOne(mappedBy = "users")
//    JwtRefreshToken jwtRefreshToken;




}
