package me.quiz_together.root.model.response.broadcast;

import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.broadcast.GiftType;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.support.hashid.HashBroadcastId;

@Data
@Builder
public class CurrentBroadcastView {
    @HashBroadcastId
    private Long broadcastId;
    private String title;
    private Long scheduledTime;
    private GiftType giftType;
    private Long prize;
    private String giftDescription;
    private BroadcastStatus broadcastStatus;
    private UserView userView;
}
