package me.quiz_together.root.model.response.broadcast;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.support.hashid.HashBroadcastId;

@Data
@Builder
public class BroadcastView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    private String title;
    private BroadcastStatus broadcastStatus;
    private String description;
    private Long scheduledTime;
    private GiftType giftType;
    private Long prize;
    private String giftDescription;
    private String winnerMessage;
    private int questionCount;

}
