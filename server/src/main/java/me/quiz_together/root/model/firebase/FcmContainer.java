package me.quiz_together.root.model.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FcmContainer<T> {
    private String to;
    private T data;
}
