package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor @Getter @Setter
public class UserFollower {
    private Long userId;
    private Long follower;
}
