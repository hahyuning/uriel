package com.uriel.travel.jwt.domain;

import com.uriel.travel.domain.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jwt_refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    @Value("${jwt.live.atk}")
    Long expiredTime;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_Id")
    Users user;
    @Column
    String refreshToken;
    public void updateRefreshToken(String refreshToken){
        this.refreshToken=refreshToken;

    }

}
