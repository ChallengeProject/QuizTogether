package me.quiz_together.root.model.request.broadcast;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class StartBroadcastReq {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    private Long scheduledTime;
    private String streamingUrl;
}
