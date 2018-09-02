package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.quiz_together.root.exceptions.InvalidUpdateException;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum BroadcastStatus implements ValueEnum {
    CREATED(100, true),
    WATING(200, true),
    OPEN_QUESTION(300, true),
    OPEN_ANSWER(400, true),
    OPEN_WINNER(500, true),
    COMPLETED(600, false);

    private int value;
    @Getter
    private boolean accessibleBroadcast;

    @Override
    public int getValue() {
        return value;
    }

    public static void validateNextBroadcastStatus(BroadcastStatus currentBroadcastStatus, BroadcastStatus nextBroadcastStatus) {
        switch (currentBroadcastStatus) {
            case CREATED:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case WATING:
                if (nextBroadcastStatus == OPEN_ANSWER || nextBroadcastStatus == OPEN_QUESTION || nextBroadcastStatus == OPEN_WINNER) {
                    return;
                }
                break;
            case OPEN_QUESTION:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case OPEN_ANSWER:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case OPEN_WINNER:
                if (nextBroadcastStatus == COMPLETED) {
                    return;
                }
                break;
        }

        throw new InvalidUpdateException("해당 status 변경 할 수 없습니다! currentStatus : " + currentBroadcastStatus.name() + " / nextStatus : " + nextBroadcastStatus.name());
    }
}
