package me.quiz_together.root.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserFollower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long userId;
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long follower;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();

    @Builder
    public UserFollower(Long userId, Long follower) {
        this.userId = userId;
        this.follower = follower;
    }
}
