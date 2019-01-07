package me.quiz_together.root.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
@Entity
@EqualsAndHashCode(of = "userId")
public class UserReferral {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long userId;
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long referralUser;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();

    @Builder
    public UserReferral(Long userId, Long referralUser) {
        this.userId = userId;
        this.referralUser = referralUser;
    }
}
