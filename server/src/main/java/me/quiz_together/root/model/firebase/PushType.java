package me.quiz_together.root.model.firebase;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum PushType implements ValueEnum {
    CHAT_MESSAGE(100),
    ADMIN_MESSAGE(200),
    QUESTION_MESSAGE(300),
    ANSWER_MESSAGE(400),
    WINNERS_MESSAGE(500),
    NOTICE_MESSAGE(600);

    private int value;

    @Override
    public int getValue() {
        return value;
    }

}
