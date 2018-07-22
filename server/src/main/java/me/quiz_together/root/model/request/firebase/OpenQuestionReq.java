package me.quiz_together.root.model.request.firebase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class OpenQuestionReq {
    private int step;
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
}
