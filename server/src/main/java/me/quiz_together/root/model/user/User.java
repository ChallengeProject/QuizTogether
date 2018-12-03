package me.quiz_together.root.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor @Getter @Setter @Builder
public class User {
    @Id @GeneratedValue
    private Long id;
    @Column(length = 40)
    private String name;
    @Column(length = 300)
    private String profilePath;
    private Long money = 0L;
    private Long createdTime;
    private Long updatedTime;
    private Long deletedTime;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.NORMAL;
}
