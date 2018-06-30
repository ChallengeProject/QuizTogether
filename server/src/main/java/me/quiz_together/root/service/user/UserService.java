package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.quiz_together.root.exceptions.ConflictUserException;
import me.quiz_together.root.exceptions.NotFoundUserException;
import me.quiz_together.root.model.request.UserLoginRequest;
import me.quiz_together.root.model.request.UserSignupRequest;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserDevice;
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

    public void insertUser(UserSignupRequest userSignupRequest) {
        // user 중복 검사
        findUserByName(userSignupRequest.getName());

        User user = buildUser(userSignupRequest);
        userRepository.insertUser(user);

        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(user.getId());
        userDevice.setPushToken(userSignupRequest.getPushToken());
        userDeviceService.insertUserDevice(userDevice);

    }

    public User getUserById(long id) {
        return userRepository.selectUserById(id);
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

    public User login(UserLoginRequest userLoginRequest) {
        return userRepository.login(userLoginRequest);
    }

    public void findUserByName(String name) {
        if (userRepository.selectUserByName(name)) {
            throw new ConflictUserException();
        }
    }

    public User buildUser(UserSignupRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());

        return user;
    }
}
