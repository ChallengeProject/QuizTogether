package me.quiz_together.root.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {
    @Id @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(length = 40, nullable = false, unique = true)
    private String name;
    @Column(length = 300, unique = true)
    private String profilePath;
    @Column(nullable = false)
    private Long money = 0L;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();
    @Column(nullable = false)
    private Long updatedTime = System.currentTimeMillis();
    private Long deletedTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus = UserStatus.NORMAL;

    @Builder
    public User(String name, String profilePath, Long money) {
        this.name = name;
        this.profilePath = profilePath;
        this.money = money;
    }
}
