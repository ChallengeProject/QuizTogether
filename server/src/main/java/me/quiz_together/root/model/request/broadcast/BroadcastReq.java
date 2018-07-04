package me.quiz_together.root.model.request.broadcast;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.request.question.QuestionReq;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class BroadcastReq {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    private String title;
    private String description;
    private Long scheduledTime;
    private GiftType giftType;
    private Long prize;
    private String giftDescription;
    private String winnerMessage;
    private List<QuestionReq> questionList;
}
