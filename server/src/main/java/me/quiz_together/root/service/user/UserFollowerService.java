package me.quiz_together.root.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.repository.user.UserFollowerRepository;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserFollowerService {
    private UserFollowerRepository userFollowerRepository;

    public void insertFollower(UserFollower userFollower) {
        try {
            userFollowerRepository.insertFollower(userFollower);
        } catch (DataIntegrityViolationException e) {
            //TODO : error code 정의
            log.warn("userId : {} follower : {}", userFollower.getUserId(), userFollower.getFollower());
            throw new DataIntegrityViolationException("userId : " + userFollower.getUserId() + " follower : " + userFollower.getFollower() , e);
        }
    }

    public void deleteFollower(UserFollower userFollower) {
        userFollowerRepository.deleteFollower(userFollower);
    }

    public List<UserFollower> getFollowerListByUserId(Long userId) {
        return userFollowerRepository.selectFollowerListByUserId(userId);
    }
}
