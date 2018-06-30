package me.quiz_together.root.model.request;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String name;
    private String pushToken;
}
