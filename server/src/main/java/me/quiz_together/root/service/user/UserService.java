package me.quiz_together.root.service.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.quiz_together.root.exceptions.ConflictUserException;
import me.quiz_together.root.exceptions.NotFoundUserException;
import me.quiz_together.root.model.request.user.UserIdReq;
import me.quiz_together.root.model.request.user.UserSignupRequest;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserDevice;
import me.quiz_together.root.model.user.UserStatus;
import me.quiz_together.root.repository.user.UserRepository;
import me.quiz_together.root.service.AmazonS3Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private AmazonS3Service amazonS3Service;

    public User insertUser(UserSignupRequest userSignupRequest) {
        // user 중복 검사
        findUserByName(userSignupRequest.getName());

        User user = buildUser(userSignupRequest);
        userRepository.insertUser(user);

        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(user.getId());
        userDevice.setPushToken(userSignupRequest.getPushToken());
        userDeviceService.insertUserDevice(userDevice);

        return user;
    }

    public User getUserById(long id) {
        return userRepository.selectUserById(id);
    }

    public Map<Long, User> getUserByIds(Collection<Long> userIds) {
        if (userIds.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        return userRepository.getUserByIds(userIds);
    }

    public void updateUserProfile(long userId, MultipartFile profileImage) {
        String profileImageUrl = amazonS3Service.uploadImage(profileImage);
        if(userRepository.updateUserProfile(userId, profileImageUrl) != 1) {
            throw new NotFoundUserException();
        }
    }

    public int deleteUserById(long id) {
        return userRepository.deleteUserById(id);
    }

    public User login(UserIdReq userIdReq) {
        return userRepository.login(userIdReq.getUserId());
    }

    public void findUserByName(String name) {
        User user = userRepository.selectUserByName(name);
        if (Objects.nonNull(user)) {
            throw new ConflictUserException();
        }
    }

    public User buildUser(UserSignupRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setUserStatus(UserStatus.NORMAL);
        user.setMoney(0L);
        return user;
    }
}
