package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor @Getter @Setter
public class UserReferral {
    private Long userId;
    private Long referralUser;
}
