package me.quiz_together.root.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
public class UserInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long seq;
    @Column(nullable = false, unique = true, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long userId;
    @Column(nullable = false)
    private int heartCount = 1;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();

    @Builder
    public UserInventory(Long userId, int heartCount) {
        this.userId = userId;
        this.heartCount = heartCount;
    }
}
