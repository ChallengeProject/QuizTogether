package me.quiz_together.root.repository.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.user.UserFollower;

@Repository
public class UserFollowerRepository {
    @Autowired
    private UserFollowerMapper userFollowerMapper;

    public void insertFollower(UserFollower userFollower) {
        try {
            userFollowerMapper.insertFollower(userFollower);
        } catch (Exception e) {

        }
    }

    public void deleteFollower(UserFollower userFollower) {
        userFollowerMapper.deleteFollower(userFollower);
    }

    public List<UserFollower> selectFollowerListByUserId(@Param("userId") Long userId) {
        return userFollowerMapper.selectFollowerListByUserId(userId);
    }
}
