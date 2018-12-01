package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor @Getter @Setter @Builder
public class User {
    private Long id;
    private String name;
    private String profilePath;
    private Long money = 0L;
    private Long createdTime;
    private Long updatedTime;
    private Long deletedTime;
    private UserStatus userStatus = UserStatus.NORMAL;
}
