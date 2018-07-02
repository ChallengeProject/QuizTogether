package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum BroadcastStatus implements ValueEnum {
    CREATED(100),
    WATING(200),
    OPEN_QUESTION(300),
    OPEN_ANSWER(400),
    OPEN_WINNER(500),
    COMPLETED(600);

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
