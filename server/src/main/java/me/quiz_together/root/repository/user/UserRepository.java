package me.quiz_together.root.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.request.UserLoginRequest;
import me.quiz_together.root.model.user.User;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public User selectUserById(long id) {
        return userMapper.selectUserById(id);
    }

    public int updateUserProfile(long userId, String profileImageUrl) {
        return userMapper.updateUserProfile(userId, profileImageUrl);
    }

    public int deleteUserById(long id) {
        return userMapper.deleteUserById(id);
    }

    public boolean selectUserByName(String name) {
        return userMapper.selectUserByName(name) > 0;
    }

    public User login(UserLoginRequest userLoginRequest) {
        return userMapper.login(userLoginRequest);
    }
}
