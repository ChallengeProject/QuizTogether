package me.quiz_together.root.exceptions;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.supoort.ExceptionCode;

@Slf4j
public class AbstractRuntimeException extends RuntimeException {
    protected ExceptionCode exceptionCode = ExceptionCode.INTERNAL_SERVER_ERROR;

    protected AbstractRuntimeException(ExceptionCode exceptionCode) {
        this(exceptionCode, exceptionCode.getMessage());
    }

    protected AbstractRuntimeException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
