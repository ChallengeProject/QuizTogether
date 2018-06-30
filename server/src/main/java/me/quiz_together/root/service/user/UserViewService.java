package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.request.UserLoginRequest;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.user.User;

@Service
public class UserViewService {
    @Autowired
    private UserService userService;

    public UserView login(UserLoginRequest userLoginRequest) {
        User user = userService.login(userLoginRequest);

        return UserView.builder()
                       .userId(user.getId())
                       .name(user.getName())
                       .build();
    }

    public UserProfileView getUserProfileView(long userId) {
        User user = userService.getUserById(userId);

        return UserProfileView.builder()
                .userId(user.getId())
                .profilePath(user.getProfilePath())
                .money(user.getMoney())
                .name(user.getName())
                .build();
    }
}
