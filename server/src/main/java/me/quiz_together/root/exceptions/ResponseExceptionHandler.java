package me.quiz_together.root.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.supoort.ExceptionCode;
import me.quiz_together.root.model.supoort.ResultContainer;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundUserException.class)
    public final ResultContainer<Object> handleNotFoundUserException(HttpServletRequest request, NotFoundUserException e) {
        log.warn(e.toString(), e);

        return new ResultContainer<>(ExceptionCode.NOT_FOUND_USER, e.toString(), e);
    }

}
