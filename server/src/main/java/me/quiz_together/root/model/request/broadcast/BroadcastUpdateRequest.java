package me.quiz_together.root.model.request.broadcast;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.support.hashid.HashBroadcastId;

@Data
public class BroadcastUpdateRequest {
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    private Long scheduledTime;
    @NotNull
    private GiftType giftType;
    @NotNull
    private Long prize;
    @NotNull
    private String giftDescription;
    @NotNull
    private String winnerMessage;
    @NotNull
    private List<QuestionView> questionList;
}
