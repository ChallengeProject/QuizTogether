package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BroadcastStatus{
    CREATED(true),
    WAITING(true),
    OPEN_QUESTION(true),
    OPEN_ANSWER(true),
    OPEN_WINNER(true),
    COMPLETED(false);

    @Getter
    private boolean accessibleBroadcast;
}
