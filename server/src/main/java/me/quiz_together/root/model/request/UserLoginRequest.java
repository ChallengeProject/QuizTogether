package me.quiz_together.root.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class UserLoginRequest {
    @ApiModelProperty("String")
    @HashUserId
    private Long userId;
}
