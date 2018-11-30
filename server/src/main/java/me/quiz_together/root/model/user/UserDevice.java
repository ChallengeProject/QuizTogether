package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDevice {
    private Long id;
    private Long userId;
    private String pushToken;
    private Long createdTime;
    private Long updatedTime;
}
