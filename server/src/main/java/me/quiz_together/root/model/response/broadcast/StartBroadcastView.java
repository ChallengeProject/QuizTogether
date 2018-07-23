package me.quiz_together.root.model.response.broadcast;

import lombok.Data;

@Data
public class StartBroadcastView {
    private String chatUrl;
    private String chatId;
    private String streamId;
    private String streamingUrl;
}
