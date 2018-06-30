package me.quiz_together.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiImplicitParam;
import me.quiz_together.root.model.request.UserIdReq;
import me.quiz_together.root.model.request.UserLoginRequest;
import me.quiz_together.root.model.request.UserSignupRequest;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.response.user.UserView;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.service.user.UserViewService;
import me.quiz_together.root.support.Constant;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;
import me.quiz_together.root.support.hashid.HashUserId;

@RestController
@RequestMapping(Constant.USER_URI)
public class UserController implements ApiController{
    @Autowired
    private UserViewService userViewService;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResultContainer<Void> signup(@RequestBody UserSignupRequest user) {
        userService.insertUser(user);

        return new ResultContainer<>();
    }

    @PostMapping("/deleteUserById")
    public ResultContainer<Void> deleteUserById(@RequestBody UserIdReq userIdReq) {
        userService.deleteUserById(userIdReq.getUserId());

        return new ResultContainer<>();
    }

    @PostMapping("/login")
    public ResultContainer<UserView> login(@RequestBody UserLoginRequest userLoginRequest) {
        return new ResultContainer<>(userViewService.login(userLoginRequest));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @PostMapping("/updateUserProfile")
    public ResultContainer<Void> updateUserProfile(@RequestParam @HashUserId Long userId,
                                                     @RequestPart MultipartFile profileImage) {
        userService.updateUserProfile(userId, profileImage);
        return new ResultContainer<>();
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/getUserProfile")
    public ResultContainer<UserProfileView> getUserProfile(@RequestParam @HashUserId Long userId) {

        return new ResultContainer<>(userViewService.getUserProfileView(userId));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/logout")
    public ResultContainer<Void> logout(@RequestParam @HashUserId Long userId) {

        return new ResultContainer<>();
    }

    @GetMapping("/findUserByName")
    public ResultContainer<Void> findUserByName(@RequestParam String name) {
        userService.findUserByName(name);
        return new ResultContainer<>();
    }

    @GetMapping("/generateId")
    public ResultContainer<String> generateId(@RequestParam Long id) {
        return new ResultContainer<>(HashIdUtils.encryptId(HashIdType.USER_ID, id));
    }
}
