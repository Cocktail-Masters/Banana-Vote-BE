package com.cocktailmasters.backend.account.user.domain.entity;

import com.cocktailmasters.backend.achievement.domain.entity.UserAchievement;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.goods.domain.entity.UserBadge;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.cocktailmasters.backend.picket.domain.entity.Picket;
import com.cocktailmasters.backend.point.domain.entity.PointLog;
import com.cocktailmasters.backend.report.domain.entity.BanLog;
import com.cocktailmasters.backend.report.domain.entity.Report;
import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;
import com.cocktailmasters.backend.vote.domain.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@Entity
public class User extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @NotNull
    private String email;

    private String password;

    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = null;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String refreshToken;

    @Builder.Default
    private Long points = 0L;

    private LocalDateTime banReleaseDate;

    @Builder.Default
    private int banNumber = 0;

    private String equippedBadgeImageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Opinion> opinions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Agreement> agreements = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Prediction> predictions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTag> userTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGoods> userGoods = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Picket> pickets = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Megaphone> megaphones = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAchievement> userAchievements = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBadge> userBadges = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PointLog> pointLogs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BanLog> banLogs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    public void usePoints(Long points) throws Exception {
        //TODO: 예외처리
        if (this.points < points) {
            throw new Exception();
        }
        this.points -= points;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
