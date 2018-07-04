package me.quiz_together.root.model.question;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum Category implements ValueEnum {
    NORMAL(0);

    private int value;
    @Override
    public int getValue() {
        return value;
    }
}
