package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Builder
@Getter
public class FollowBroadcastMessage {
    @HashBroadcastId
    private Long broadcastId;
    @HashUserId
    private String title;
    private String description;
    private String userName;
    private PushType pushType;
}
