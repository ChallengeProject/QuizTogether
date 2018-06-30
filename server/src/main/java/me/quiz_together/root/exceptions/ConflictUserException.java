package me.quiz_together.root.exceptions;

import me.quiz_together.root.model.supoort.ExceptionCode;

public class ConflictUserException extends AbstractRuntimeException {

    public ConflictUserException() {
        super(ExceptionCode.CONFLICT_USER, ExceptionCode.CONFLICT_USER.getMessage());
    }
}
