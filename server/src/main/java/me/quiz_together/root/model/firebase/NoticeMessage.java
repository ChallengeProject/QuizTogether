package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Builder
@Getter
public class NoticeMessage {
    @HashBroadcastId
    private Long broadcastId;
    @HashUserId
    private Long userId;
    private String title;
    private PushType pushType;
}
