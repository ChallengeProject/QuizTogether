package me.quiz_together.root.model.response.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class UserFollowerView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long follower;

    public UserFollowerView(UserFollower userFollower) {
        this.follower = userFollower.getFollower();
    }
}
