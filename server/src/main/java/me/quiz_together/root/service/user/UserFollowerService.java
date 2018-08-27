package me.quiz_together.root.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.repository.user.UserFollowerRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserFollowerService {
    private UserFollowerRepository userFollowerRepository;

    public void insertFollower(UserFollower userFollower) {
        userFollowerRepository.insertFollower(userFollower);
    }

    public void deleteFollower(UserFollower userFollower) {
        userFollowerRepository.deleteFollower(userFollower);
    }

    public List<UserFollower> getFollowerListByUserId(Long userId) {
        return userFollowerRepository.selectFollowerListByUserId(userId);
    }
}
