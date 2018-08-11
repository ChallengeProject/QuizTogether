package me.quiz_together.root.model.response.broadcast;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartBroadcastView {
    private String chatUrl;
    private String chatId;
    private String streamId;
    private String streamingUrl;
    private BroadcastView broadcastView;
}
