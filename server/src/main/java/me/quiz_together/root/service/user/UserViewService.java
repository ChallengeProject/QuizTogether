package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.quiz_together.root.model.request.user.UserIdReq;
import me.quiz_together.root.model.request.user.UserSignupRequest;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.user.User;

@Service
public class UserViewService {
    @Autowired
    private UserService userService;

    public UserView login(UserIdReq userIdReq) {
        User user = userService.login(userIdReq);

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

    public UserView insertUser(UserSignupRequest userSignupRequest) {
        User user = userService.insertUser(userSignupRequest);

        return UserView.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
    }

    public void deleteUserById(UserIdReq userIdReq) {
        userService.deleteUserById(userIdReq.getUserId());
    }

    public void updateUserProfile(long userId, MultipartFile profileImage) {
        userService.updateUserProfile(userId, profileImage);
    }

    public void findUserByName(String name) {
        userService.findUserByName(name);
    }
}
