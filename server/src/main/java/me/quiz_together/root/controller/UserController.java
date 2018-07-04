package me.quiz_together.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiImplicitParam;
import me.quiz_together.root.model.request.user.UserIdReq;
import me.quiz_together.root.model.request.user.UserSignupRequest;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.user.UserViewService;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;
import me.quiz_together.root.support.hashid.HashUserId;

@RestController
public class UserController implements ApiController {
    @Autowired
    private UserViewService userViewService;

    @PostMapping("/user/signup")
    public ResultContainer<UserView> signup(@RequestBody UserSignupRequest user) {

        return new ResultContainer<>(userViewService.insertUser(user));
    }

    @PostMapping("/user/deleteUserById")
    public ResultContainer<Void> deleteUserById(@RequestBody UserIdReq userIdReq) {
        userViewService.deleteUserById(userIdReq);

        return new ResultContainer<>();
    }

    @PostMapping("/user/login")
    public ResultContainer<UserView> login(@RequestBody UserIdReq userIdReq) {
        return new ResultContainer<>(userViewService.login(userIdReq));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @PostMapping("/user/updateUserProfile")
    public ResultContainer<Void> updateUserProfile(@RequestParam @HashUserId Long userId,
                                                     @RequestPart MultipartFile profileImage) {
        userViewService.updateUserProfile(userId, profileImage);
        return new ResultContainer<>();
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/user/getUserProfile")
    public ResultContainer<UserProfileView> getUserProfile(@RequestParam @HashUserId Long userId) {

        return new ResultContainer<>(userViewService.getUserProfileView(userId));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/user/logout")
    public ResultContainer<Void> logout(@RequestParam @HashUserId Long userId) {

        return new ResultContainer<>();
    }

    @GetMapping("/user/findUserByName")
    public ResultContainer<Void> findUserByName(@RequestParam String name) {
        userViewService.findUserByName(name);
        return new ResultContainer<>();
    }

    @GetMapping("/user/generateId")
    public ResultContainer<String> generateId(@RequestParam Long id) {
        return new ResultContainer<>(HashIdUtils.encryptId(HashIdType.USER_ID, id));
    }
}
