package me.quiz_together.root.model.broadcast;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum BroadcastStatus implements ValueEnum {
    CREATED(100, false, Sets.newHashSet(BroadcastStatus.WATING)),
    WATING(200, true, Sets.newHashSet(BroadcastStatus.OPEN_QUESTION,
                                     BroadcastStatus.OPEN_ANSWER)),
    OPEN_QUESTION(300, true, Sets.newHashSet(BroadcastStatus.WATING)),
    OPEN_ANSWER(400, true, Sets.newHashSet(BroadcastStatus.WATING,
                                           BroadcastStatus.OPEN_WINNER)),
    OPEN_WINNER(500, true, Sets.newHashSet(BroadcastStatus.COMPLETED)),
    COMPLETED(600, false, Sets.newHashSet());

    private int value;
    @Getter
    private boolean accessibleBroadcast;
    @Getter
    private Set<BroadcastStatus> nextBroadcastStatus;

    @Override
    public int getValue() {
        return value;
    }

    public static boolean validateNextBroadcastStatus(BroadcastStatus currentBroadcastStatus, BroadcastStatus nextBroadcastStatus) {
        return currentBroadcastStatus.getNextBroadcastStatus().contains(nextBroadcastStatus);
    }
}
